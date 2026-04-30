package com.searchplatform.searchservice.service.impl;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.aggregations.AggregationRange;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.json.JsonData;

import com.searchplatform.searchservice.model.FacetBucket;
import com.searchplatform.searchservice.model.ProductSearchResponse;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.ProductSearchService;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final OpenSearchClient client;

    public ProductSearchServiceImpl(OpenSearchClient client) {
        this.client = client;
    }

    @Override
    public ProductSearchResponse searchProducts(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax,
            int page,
            int size,
            String sortField,
            String order
    ) throws IOException {

        SortOrder sortOrder = "desc".equalsIgnoreCase(order)
                ? SortOrder.Desc
                : SortOrder.Asc;

        List<AggregationRange> priceRanges = List.of(
                AggregationRange.of(r -> r.to("10000")),
                AggregationRange.of(r -> r.from("10000").to("50000")),
                AggregationRange.of(r -> r.from("50000"))
        );

        SearchResponse<SearchProductDocument> response = client.search(s -> {

            s.index("products_v2")
                    .from(page * size)
                    .size(size);

            s.query(q -> q.bool(b -> {

                // 🔍 SEARCH
                if (query != null && !query.trim().isBlank()) {

                    b.should(m -> m.multiMatch(mm -> mm
                            .query(query)
                            .type(TextQueryType.BoolPrefix)
                            .fields(
                                    "name^5",
                                    "name._2gram^4",
                                    "name._3gram^3",
                                    "brand^2",
                                    "category^2",
                                    "description"
                            )
                    ));

                    if (query.trim().length() >= 4) {
                        b.should(sq -> sq.match(m -> m
                                .field("name")
                                .query(FieldValue.of(query))
                                .fuzziness("AUTO")
                                .boost(2.0f)
                        ));
                    }

                    b.minimumShouldMatch("1");
                }

                // 🎯 FILTERS
                if (brand != null && !brand.isBlank()) {
                    b.filter(f -> f.term(t -> t
                            .field("brand") // ⚠️ important fix
                            .value(FieldValue.of(brand))));
                }

                if (category != null && !category.isBlank()) {
                    b.filter(f -> f.term(t -> t
                            .field("category") // ⚠️ important fix
                            .value(FieldValue.of(category))));
                }

                if (priceMin != null || priceMax != null) {
                    b.filter(f -> f.range(r -> {
                        r.field("price");

                        if (priceMin != null) {
                            r.gte(JsonData.of(priceMin));
                        }

                        if (priceMax != null) {
                            r.lte(JsonData.of(priceMax));
                        }

                        return r;
                    }));
                }

                return b;
            }));

            // 🔽 SORT
            if (sortField != null) {
                s.sort(sort -> sort.field(f -> f
                        .field(sortField)
                        .order(sortOrder)
                ));
            }

            // 📊 AGGREGATIONS
            s.aggregations("brands",
                    a -> a.terms(t -> t.field("brand")));

            s.aggregations("categories",
                    a -> a.terms(t -> t.field("category")));

            s.aggregations("price_ranges",
                    a -> a.range(r -> r
                            .field("price")
                            .ranges(priceRanges)
                    )
            );

            return s;

        }, SearchProductDocument.class);

        // 📦 RESULTS
        List<SearchProductDocument> products =
                response.hits().hits().stream()
                        .map(Hit::source)
                        .toList();

        List<FacetBucket> brandFacets =
                response.aggregations().get("brands")
                        .sterms().buckets().array().stream()
                        .map(bucket -> new FacetBucket(
                                bucket.key(),
                                bucket.docCount()))
                        .toList();

        List<FacetBucket> categoryFacets =
                response.aggregations().get("categories")
                        .sterms().buckets().array().stream()
                        .map(bucket -> new FacetBucket(
                                bucket.key(),
                                bucket.docCount()))
                        .toList();

        List<FacetBucket> priceFacets =
                response.aggregations().get("price_ranges")
                        .range().buckets().array().stream()
                        .map(bucket -> new FacetBucket(
                                bucket.key(),
                                bucket.docCount()))
                        .toList();

        long total = response.hits().total().value();

        return new ProductSearchResponse(
                products,
                brandFacets,
                categoryFacets,
                priceFacets,
                total,
                page,
                size
        );
    }
}
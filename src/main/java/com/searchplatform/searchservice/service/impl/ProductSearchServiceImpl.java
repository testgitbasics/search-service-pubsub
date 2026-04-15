package com.searchplatform.searchservice.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.searchplatform.searchservice.model.FacetBucket;
import com.searchplatform.searchservice.model.ProductSearchResponse;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.ProductSearchService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ElasticsearchClient client;

    public ProductSearchServiceImpl(ElasticsearchClient client) {
        this.client = client;
    }


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

        {
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

                s.query(q -> q
                        .bool(b -> {

                            // Apply search only if query exists
                            if (query != null && !query.trim().isBlank()) {

                                // Main search: prefix / partial / brand / category / description
                                b.should(m -> m
                                        .multiMatch(mm -> mm
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
                                        )
                                );

                                // Fuzzy search: typo tolerance on product name
                                if (query.trim().length() >= 4) {
                                    b.should(sq -> sq
                                            .match(m -> m
                                                    .field("name")
                                                    .query(query)
                                                    .fuzziness("AUTO")
                                                    .boost(2.0f)
                                            )
                                    );
                                }

                                // At least one search clause should match
                                b.minimumShouldMatch("1");
                            }

                            // Filters
                            if (brand != null && !brand.isBlank()) {
                                b.filter(f -> f.term(t -> t
                                        .field("brand")
                                        .value(brand)));
                            }

                            if (category != null && !category.isBlank()) {
                                b.filter(f -> f.term(t -> t
                                        .field("category")
                                        .value(category)));
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
                        })
                );

                if (sortField != null) {
                    s.sort(sort -> sort
                            .field(f -> f
                                    .field(sortField)
                                    .order(sortOrder)
                            )
                    );
                }

                // FACETS
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

            List<SearchProductDocument> products =
                    response.hits()
                            .hits()
                            .stream()
                            .map(hit -> hit.source())
                            .toList();

            List<FacetBucket> brandFacets =
                    response.aggregations()
                            .get("brands")
                            .sterms()
                            .buckets()
                            .array()
                            .stream()
                            .map(bucket -> new FacetBucket(
                                    bucket.key().stringValue(),
                                    bucket.docCount()))
                            .toList();

            List<FacetBucket> categoryFacets =
                    response.aggregations()
                            .get("categories")
                            .sterms()
                            .buckets()
                            .array()
                            .stream()
                            .map(bucket -> new FacetBucket(
                                    bucket.key().stringValue(),
                                    bucket.docCount()))
                            .toList();

            List<FacetBucket> priceFacets =
                    response.aggregations()
                            .get("price_ranges")
                            .range()
                            .buckets()
                            .array()
                            .stream()
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
}

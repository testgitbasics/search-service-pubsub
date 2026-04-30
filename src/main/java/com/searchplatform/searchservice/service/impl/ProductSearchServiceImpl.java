package com.searchplatform.searchservice.service.impl;

import com.searchplatform.searchservice.model.FacetBucket;
import com.searchplatform.searchservice.model.ProductSearchResponse;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.repository.ProductSearchRepository;
import com.searchplatform.searchservice.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductSearchRepository repository;

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
    ) {

        String orderByClause = "score DESC"; // default

        if (sortField != null) {
            String direction = "asc".equalsIgnoreCase(order) ? "ASC" : "DESC";

            switch (sortField) {
                case "price":
                    orderByClause = "price " + direction + ", score DESC";
                    break;
                case "rating":
                    orderByClause = "rating " + direction + ", score DESC";
                    break;
                default:
                    orderByClause = "score DESC";
            }
        }

        // 🔹 Fetch data
        List<SearchProductDocument> products =
                repository.searchProducts(query, brand, category, priceMin, priceMax, page, size, orderByClause);

        List<FacetBucket> brandFacets =
                repository.getBrandFacets(query, brand, category, priceMin, priceMax);

        List<FacetBucket> categoryFacets =
                repository.getCategoryFacets(query, brand, category, priceMin, priceMax);

        List<FacetBucket> priceFacets =
                repository.getPriceFacets(query, brand, category, priceMin, priceMax);

        // 🔹 Total count (simple version)
        long total = repository.getTotalCount(
                query, brand, category, priceMin, priceMax
        );// can optimize later

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

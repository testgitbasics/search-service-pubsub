package com.searchplatform.searchservice.service;

import com.searchplatform.searchservice.model.ProductSearchResponse;
import java.io.IOException;
public interface ProductSearchService {
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
    ) throws IOException;
}

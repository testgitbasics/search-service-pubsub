package com.searchplatform.searchservice.controller;

import com.searchplatform.searchservice.model.ProductSearchResponse;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.ProductSearchService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    private final ProductSearchService service;

    public SearchController(ProductSearchService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ProductSearchResponse searchProducts(
            @RequestParam String q,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "asc") String order
    ) throws IOException {

        return service.searchProducts(
                q,
                brand,
                category,
                priceMin,
                priceMax,
                page,
                size,
                sort,
                order
        );
    }
}

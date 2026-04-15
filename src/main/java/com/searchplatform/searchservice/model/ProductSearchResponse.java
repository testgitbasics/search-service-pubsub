package com.searchplatform.searchservice.model;

import java.util.List;

public class ProductSearchResponse {

    private List<SearchProductDocument> products;
    private List<FacetBucket> brands;
    private List<FacetBucket> categories;
    private List<FacetBucket> prices;

    private long total;
    private int page;
    private int size;

    public ProductSearchResponse(
            List<SearchProductDocument> products,
            List<FacetBucket> brands,
            List<FacetBucket> categories,
            List<FacetBucket> prices,
            long total,
            int page,
            int size) {

        this.products = products;
        this.brands = brands;
        this.categories = categories;
        this.prices = prices;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<SearchProductDocument> getProducts() {
        return products;
    }

    public List<FacetBucket> getBrands() {
        return brands;
    }

    public List<FacetBucket> getCategories() {
        return categories;
    }

    public List<FacetBucket> getPrices() {
        return prices;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}

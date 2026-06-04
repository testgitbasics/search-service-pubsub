package com.searchplatform.searchservice.searchframework.model;

import java.util.Map;

public class SearchRequest {

    private String query;

    private Map<String, Object> filters;

    private Integer page = 0;

    private Integer size = 10;

    private String order = "DESC";

    private String sortBy;

    private String sortDirection;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortBy(
            String sortBy
    ) {
        this.sortBy = sortBy;
    }

    public void setSortDirection(
            String sortDirection
    ) {
        this.sortDirection = sortDirection;
    }
}
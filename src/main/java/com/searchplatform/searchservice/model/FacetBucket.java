package com.searchplatform.searchservice.model;

public class FacetBucket {

    private String name;
    private long count;

    public FacetBucket(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }
}

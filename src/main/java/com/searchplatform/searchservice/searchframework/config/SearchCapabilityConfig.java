package com.searchplatform.searchservice.searchframework.config;

public class SearchCapabilityConfig {

    private Boolean fuzzy = false;

    public Boolean getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(
            Boolean fuzzy
    ) {
        this.fuzzy = fuzzy;
    }
}
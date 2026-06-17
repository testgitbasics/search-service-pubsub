package com.searchplatform.searchservice.searchframework.config;

public class SearchConfig {

    private SearchMode mode =
            SearchMode.FULL_TEXT;

    private SearchCapabilityConfig capabilities =
            new SearchCapabilityConfig();

    public Double getFuzzyThreshold() {
        return fuzzyThreshold;
    }

    public void setFuzzyThreshold(Double fuzzyThreshold) {
        this.fuzzyThreshold = fuzzyThreshold;
    }

    private Double fuzzyThreshold = 0.2;

    public SearchMode getMode() {
        return mode;
    }

    public void setMode(
            SearchMode mode
    ) {
        this.mode = mode;
    }

    public SearchCapabilityConfig getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(
            SearchCapabilityConfig capabilities
    ) {
        this.capabilities = capabilities;
    }
}

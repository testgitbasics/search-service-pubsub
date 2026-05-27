package com.searchplatform.searchservice.searchframework.config;

public class FeatureConfig {

    private boolean fullTextSearch = true;

    private boolean autocomplete = true;

    private boolean fuzzySearch = true;

    private boolean ranking = true;

    private boolean pagination = true;

    private boolean sorting = true;

    public boolean isFullTextSearch() {
        return fullTextSearch;
    }

    public void setFullTextSearch(boolean fullTextSearch) {
        this.fullTextSearch = fullTextSearch;
    }

    public boolean isAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(boolean autocomplete) {
        this.autocomplete = autocomplete;
    }

    public boolean isFuzzySearch() {
        return fuzzySearch;
    }

    public void setFuzzySearch(boolean fuzzySearch) {
        this.fuzzySearch = fuzzySearch;
    }

    public boolean isRanking() {
        return ranking;
    }

    public void setRanking(boolean ranking) {
        this.ranking = ranking;
    }

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public boolean isSorting() {
        return sorting;
    }

    public void setSorting(boolean sorting) {
        this.sorting = sorting;
    }
}
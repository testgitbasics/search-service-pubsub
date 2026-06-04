package com.searchplatform.searchservice.searchframework.config;

public class FeatureConfig {

    private Boolean autocomplete = true;

    private Boolean ranking = true;

    private Boolean pagination = true;

    public Boolean getAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(
            Boolean autocomplete
    ) {
        this.autocomplete = autocomplete;
    }

    public Boolean getRanking() {
        return ranking;
    }

    public void setRanking(
            Boolean ranking
    ) {
        this.ranking = ranking;
    }

    public Boolean getPagination() {
        return pagination;
    }

    public void setPagination(
            Boolean pagination
    ) {
        this.pagination = pagination;
    }
}
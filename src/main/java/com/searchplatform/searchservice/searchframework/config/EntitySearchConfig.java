package com.searchplatform.searchservice.searchframework.config;

import java.util.ArrayList;
import java.util.List;

public class EntitySearchConfig {

    private String tableName;

    private List<String> searchFields =
            new ArrayList<>();

    private List<String> sortableFields =
            new ArrayList<>();

    private SearchConfig search =
            new SearchConfig();

    private AutoCompleteConfig autocomplete =
            new AutoCompleteConfig();

    private List<String> filterableFields =
            new ArrayList<>();


    private FeatureConfig features =
            new FeatureConfig();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public List<String> getFilterableFields() {
        return filterableFields;
    }

    public void setFilterableFields(
            List<String> filterableFields
    ) {
        this.filterableFields = filterableFields;
    }

    public SearchConfig getSearch() {
        return search;
    }

    public void setSearch(
            SearchConfig search
    ) {
        this.search = search;
    }

    public FeatureConfig getFeatures() {
        return features;
    }

    public void setFeatures(
            FeatureConfig features
    ) {
        this.features = features;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(
            List<String> searchFields
    ) {
        this.searchFields = searchFields;
    }

    public AutoCompleteConfig getAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(
            AutoCompleteConfig autocomplete
    ) {
        this.autocomplete = autocomplete;
    }

    public List<String> getSortableFields() {
        return sortableFields;
    }

    public void setSortableFields(
            List<String> sortableFields
    ) {
        this.sortableFields = sortableFields;
    }
}
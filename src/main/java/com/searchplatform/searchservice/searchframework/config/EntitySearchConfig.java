package com.searchplatform.searchservice.searchframework.config;

import java.util.List;

public class EntitySearchConfig {

    private String entityName;

    private String tableName;

    private List<String> searchFields;

    private List<String> autocompleteFields;

    private List<String> sortableFields;

    private List<String> filterableFields;

    private FeatureConfig features;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<String> searchFields) {
        this.searchFields = searchFields;
    }

    public List<String> getAutocompleteFields() {
        return autocompleteFields;
    }

    public void setAutocompleteFields(List<String> autocompleteFields) {
        this.autocompleteFields = autocompleteFields;
    }

    public List<String> getSortableFields() {
        return sortableFields;
    }

    public void setSortableFields(List<String> sortableFields) {
        this.sortableFields = sortableFields;
    }

    public List<String> getFilterableFields() {
        return filterableFields;
    }

    public void setFilterableFields(List<String> filterableFields) {
        this.filterableFields = filterableFields;
    }

    public FeatureConfig getFeatures() {
        return features;
    }

    public void setFeatures(FeatureConfig features) {
        this.features = features;
    }
}
package com.searchplatform.searchservice.searchframework.config;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteConfig {

    private Boolean enabled = true;

    private List<String> fields =
            new ArrayList<>();

    private List<String> fuzzyExcludedFields =
            new ArrayList<>();

    public Double getFuzzyThreshold() {
        return fuzzyThreshold;
    }

    public void setFuzzyThreshold(Double fuzzyThreshold) {
        this.fuzzyThreshold = fuzzyThreshold;
    }

    private Double fuzzyThreshold = 0.2;

    private AutoCompleteCapabilityConfig capabilities =
            new AutoCompleteCapabilityConfig();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(
            Boolean enabled
    ) {
        this.enabled = enabled;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(
            List<String> fields
    ) {
        this.fields = fields;
    }

    public List<String> getFuzzyExcludedFields() {
        return fuzzyExcludedFields;
    }

    public void setFuzzyExcludedFields(
            List<String> fuzzyExcludedFields
    ) {
        this.fuzzyExcludedFields =
                fuzzyExcludedFields;
    }

    public AutoCompleteCapabilityConfig getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(
            AutoCompleteCapabilityConfig capabilities
    ) {
        this.capabilities = capabilities;
    }
}
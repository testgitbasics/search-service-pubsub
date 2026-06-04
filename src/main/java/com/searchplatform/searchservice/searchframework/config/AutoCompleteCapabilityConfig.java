package com.searchplatform.searchservice.searchframework.config;

public class AutoCompleteCapabilityConfig {

    private Boolean prefix = true;

    private Boolean fuzzy = true;

    public Boolean getPrefix() {
        return prefix;
    }

    public void setPrefix(
            Boolean prefix
    ) {
        this.prefix = prefix;
    }

    public Boolean getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(
            Boolean fuzzy
    ) {
        this.fuzzy = fuzzy;
    }
}

package com.searchplatform.searchservice.searchframework.config;

public class SearchCapabilityConfig {

    private Boolean fullText = true;

    private Boolean prefix = true;

    private Boolean fuzzy = true;

    public Boolean getFullText() {
        return fullText;
    }

    public void setFullText(
            Boolean fullText
    ) {
        this.fullText = fullText;
    }

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
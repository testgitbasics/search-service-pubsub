package com.searchplatform.searchservice.searchframework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(
        prefix = "search-framework"
)
public class SearchFrameworkProperties {

    private Map<String, EntitySearchConfig> entities =
            new HashMap<>();

    public Map<String, EntitySearchConfig> getEntities() {
        return entities;
    }

    public void setEntities(
            Map<String, EntitySearchConfig> entities
    ) {
        this.entities = entities;
    }
}

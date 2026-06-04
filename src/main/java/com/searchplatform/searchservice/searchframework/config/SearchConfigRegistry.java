package com.searchplatform.searchservice.searchframework.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchConfigRegistry {

    private final SearchFrameworkProperties properties;

    private Map<String, EntitySearchConfig> configs;

    public SearchConfigRegistry(
            SearchFrameworkProperties properties
    ) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {

        this.configs =
                properties.getEntities();

        validateConfigs();
    }

    private void validateConfigs() {

        configs.forEach((entity, config) -> {

            if (config.getTableName() == null
                    || config.getTableName().isBlank()) {

                throw new RuntimeException(
                        "table-name missing for entity: "
                                + entity
                );
            }
        });
    }

    public EntitySearchConfig get(
            String entity
    ) {

        EntitySearchConfig config =
                configs.get(entity);

        if (config == null) {

            throw new RuntimeException(
                    "No config found for entity: "
                            + entity
            );
        }

        return config;
    }
}
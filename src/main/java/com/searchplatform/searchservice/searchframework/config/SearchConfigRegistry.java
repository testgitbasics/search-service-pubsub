package com.searchplatform.searchservice.searchframework.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchConfigRegistry {

    private final Map<String, EntitySearchConfig> configs =
            new HashMap<>();

    @PostConstruct
    public void init() {

        FeatureConfig featureConfig =
                new FeatureConfig();

        EntitySearchConfig vendorConfig =
                new EntitySearchConfig();

        vendorConfig.setEntityName("vendor");

        vendorConfig.setTableName("vendor_search");

        vendorConfig.setSearchFields(
                List.of(
                        "vendor_name",
                        "company_name",
                        "vendor_number"
                )
        );

        vendorConfig.setAutocompleteFields(
                List.of("vendor_name")
        );

        vendorConfig.setSortableFields(
                List.of(
                        "vendor_name",
                        "vendor_number"
                )
        );

        vendorConfig.setFilterableFields(
                List.of(
                        "department",
                        "supplier_status"
                )
        );

        vendorConfig.setFeatures(featureConfig);

        configs.put(
                "vendor",
                vendorConfig
        );
    }

    public EntitySearchConfig get(
            String entity
    ) {
        return configs.get(entity);
    }
}
package com.searchplatform.searchservice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {

    private String eventType;
    private String eventVersion;
    private ProductData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private UUID id;
        private String sku;
        private String name;
        private String description;
        private String brand;
        private String category;
        private Double price;
        private Double rating;
    }
}
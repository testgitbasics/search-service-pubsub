package com.searchplatform.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    private String eventId;
    private String eventType;
    private Long productId;
    private String name;
    private String brand;
    private String category;
    private String description;
    private Double price;
    private String timestamp;
}
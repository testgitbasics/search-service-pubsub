package com.searchplatform.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductDocument {

    private String id;
    private String sku;
    private String name;
    private String description;
    private String brand;
    private String category;
    private Double price;
    private Double rating;

}

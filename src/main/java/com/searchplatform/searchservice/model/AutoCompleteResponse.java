package com.searchplatform.searchservice.model;

public class AutoCompleteResponse {

    private String vendorName;
    private Long vendorNumber;
    private Double score;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Long getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(Long vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
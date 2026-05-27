package com.searchplatform.searchservice.searchframework.model;

public class AutoCompleteResponse {

    private String value;

    private Double score;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
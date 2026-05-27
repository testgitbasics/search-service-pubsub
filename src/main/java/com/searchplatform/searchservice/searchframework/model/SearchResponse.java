package com.searchplatform.searchservice.searchframework.model;

import java.util.List;
import java.util.Map;

public class SearchResponse {

    private List<Map<String, Object>> data;

    private Long total;

    private Integer page;

    private Integer size;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(
            List<Map<String, Object>> data
    ) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

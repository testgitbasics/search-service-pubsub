package com.searchplatform.searchservice.model;

import java.util.List;

public class VendorSearchResponse {

    private List<VendorDocument> vendors;
    private long total;

    public VendorSearchResponse(List<VendorDocument> vendors, long total) {
        this.vendors = vendors;
        this.total = total;
    }

    public List<VendorDocument> getVendors() {
        return vendors;
    }

    public void setVendors(List<VendorDocument> vendors) {
        this.vendors = vendors;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

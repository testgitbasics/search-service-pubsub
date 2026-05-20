package com.searchplatform.searchservice.service;

import com.searchplatform.searchservice.model.AutoCompleteResponse;
import com.searchplatform.searchservice.model.VendorDocument;
import com.searchplatform.searchservice.model.VendorSearchResponse;
import com.searchplatform.searchservice.repository.VendorSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorSearchService {

    private final VendorSearchRepository repository;

    public VendorSearchService(VendorSearchRepository repository) {
        this.repository = repository;
    }

    public List<AutoCompleteResponse> autoComplete(String query) {
        return repository.autoComplete(query);
    }

    public VendorSearchResponse search(
            String query,
            Integer department,
            int page,
            int size
    ) {

        List<VendorDocument> vendors =
                repository.search(query, department, page, size);

        return new VendorSearchResponse(vendors, vendors.size());
    }
}

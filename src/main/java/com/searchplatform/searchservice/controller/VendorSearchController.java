package com.searchplatform.searchservice.controller;
import com.searchplatform.searchservice.model.AutoCompleteResponse;
import com.searchplatform.searchservice.model.VendorSearchResponse;
import com.searchplatform.searchservice.service.VendorSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorSearchController {

    private final VendorSearchService service;

    public VendorSearchController(VendorSearchService service) {
        this.service = service;
    }

    @GetMapping("/autocomplete")
    public List<AutoCompleteResponse> autoComplete(
            @RequestParam String q
    ) {
        return service.autoComplete(q);
    }

    @GetMapping("/search")
    public VendorSearchResponse search(
            @RequestParam String q,
            @RequestParam(required = false) Integer department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.search(q, department, page, size);
    }
}

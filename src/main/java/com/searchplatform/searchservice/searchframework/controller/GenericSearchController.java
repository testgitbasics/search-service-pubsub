package com.searchplatform.searchservice.searchframework.controller;

import com.searchplatform.searchservice.searchframework.model.AutoCompleteResponse;
import com.searchplatform.searchservice.searchframework.model.SearchRequest;
import com.searchplatform.searchservice.searchframework.model.SearchResponse;
import com.searchplatform.searchservice.searchframework.service.GenericSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class GenericSearchController {

    private final GenericSearchService service;

    public GenericSearchController(
            GenericSearchService service
    ) {
        this.service = service;
    }

    @GetMapping("/{entity}/autocomplete")
    public List<AutoCompleteResponse> autoComplete(
            @PathVariable String entity,
            @RequestParam String q
    ) {

        return service.autoComplete(
                entity,
                q
        );
    }

    @PostMapping("/{entity}")
    public SearchResponse search(
            @PathVariable String entity,
            @RequestBody SearchRequest request
    ) {

        return service.search(
                entity,
                request
        );
    }
}
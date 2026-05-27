package com.searchplatform.searchservice.searchframework.service;

import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import com.searchplatform.searchservice.searchframework.config.SearchConfigRegistry;
import com.searchplatform.searchservice.searchframework.model.AutoCompleteResponse;
import com.searchplatform.searchservice.searchframework.model.SearchRequest;
import com.searchplatform.searchservice.searchframework.model.SearchResponse;
import com.searchplatform.searchservice.searchframework.repository.GenericSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericSearchService {

    private final SearchConfigRegistry registry;

    private final GenericSearchRepository repository;

    public GenericSearchService(
            SearchConfigRegistry registry,
            GenericSearchRepository repository
    ) {
        this.registry = registry;
        this.repository = repository;
    }

    public List<AutoCompleteResponse> autoComplete(
            String entity,
            String query
    ) {

        EntitySearchConfig config =
                registry.get(entity);

        return repository.autoComplete(
                config,
                query
        );
    }

    public SearchResponse search(
            String entity,
            SearchRequest request
    ) {

        EntitySearchConfig config =
                registry.get(entity);

        return repository.search(
                config,
                request
        );
    }
}
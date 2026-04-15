package com.searchplatform.searchservice.service.adapter;

import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.SearchEngine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class InMemorySearchEngine implements SearchEngine {

    private final List<SearchProductDocument> index = new ArrayList<>();

    @Override
    public void indexProduct(SearchProductDocument document) {
        index.add(document);
        System.out.println("Indexed product into search engine:");
        System.out.println(document.getName());
    }
}

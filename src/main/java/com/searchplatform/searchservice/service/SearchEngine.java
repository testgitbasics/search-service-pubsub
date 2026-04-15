package com.searchplatform.searchservice.service;

import com.searchplatform.searchservice.model.SearchProductDocument;

public interface SearchEngine {
    void indexProduct(SearchProductDocument document);
}

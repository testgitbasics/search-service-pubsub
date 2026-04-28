package com.searchplatform.searchservice.service.adapter;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.stereotype.Service;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.SearchEngine;

@Service
public class OpenSearchAdapter implements SearchEngine {

    private final OpenSearchClient client;

    public OpenSearchAdapter(OpenSearchClient client) {
        this.client = client;
    }

    @Override
    public void indexProduct(SearchProductDocument document) {
        try {

            client.index(i -> i
                    .index("products_v2")
                    .id(document.getId())
                    .document(document)
            );

            System.out.println("Indexed product into OpenSearch: "
                    + document.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.searchplatform.searchservice.service.adapter;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.SearchEngine;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchAdapter implements SearchEngine {

    private final ElasticsearchClient client;

    public ElasticSearchAdapter(ElasticsearchClient client) {
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

            System.out.println("Indexed product into Elasticsearch: "
                    + document.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


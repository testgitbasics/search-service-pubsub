package com.searchplatform.searchservice.searchframework.builder;

import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import com.searchplatform.searchservice.searchframework.model.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class SearchQueryBuilder {

    public String build(
            EntitySearchConfig config,
            SearchRequest request
    ) {

        StringBuilder sql =
                new StringBuilder();

        sql.append("""
                SELECT *,
                ts_rank(
                    search_vector,
                    websearch_to_tsquery('english', :query)
                ) AS score
                FROM
                """);

        sql.append(config.getTableName());

        sql.append("""
                
                WHERE
                search_vector @@
                websearch_to_tsquery(
                    'english',
                    :query
                )
                """);

        if (request.getFilters() != null) {

            request.getFilters()
                    .forEach((key, value) -> {

                        if (config.getFilterableFields()
                                .contains(key)) {

                            sql.append(
                                    " AND "
                                            + key
                                            + " = :"
                                            + key
                            );
                        }
                    });
        }

        sql.append("""
                
                ORDER BY score DESC
                """);

        sql.append("""
                
                LIMIT :size
                OFFSET :offset
                """);

        return sql.toString();
    }
}

package com.searchplatform.searchservice.searchframework.builder;

import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import com.searchplatform.searchservice.searchframework.config.SearchMode;
import com.searchplatform.searchservice.searchframework.model.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class SearchQueryBuilder {

    public String build(
            EntitySearchConfig config,
            SearchRequest request
    ) {

        SearchMode mode =
                config.getSearch()
                        .getMode();

        boolean rankingEnabled =
                config.getFeatures()
                        .getRanking();

        boolean paginationEnabled =
                config.getFeatures()
                        .getPagination();

        StringBuilder sql =
                new StringBuilder();

        sql.append("SELECT *");

        if (rankingEnabled
                && mode == SearchMode.FULL_TEXT) {

            sql.append("""
            ,
            ts_rank(
                search_vector,
                websearch_to_tsquery(
                    'english',
                    :query
                )
            ) AS score
            """);
        }

        sql.append("""
                
                FROM
                """);

        sql.append(config.getTableName());

        sql.append(" WHERE ");

        if (mode == SearchMode.EXACT) {

            appendExactConditions(
                    sql,
                    config
            );

        } else {

            appendFullTextConditions(
                    sql
            );
        }

        appendFilters(
                sql,
                config,
                request
        );

        appendSorting(
                sql,
                config,
                request,
                rankingEnabled
        );

        if (paginationEnabled) {

            sql.append("""
                    
                    LIMIT :size
                    OFFSET :offset
                    """);
        }

        return sql.toString();
    }

    public String buildCountQuery(
            EntitySearchConfig config,
            SearchRequest request
    ) {

        SearchMode mode =
                config.getSearch()
                        .getMode();

        StringBuilder sql =
                new StringBuilder();

        sql.append("""
                
                SELECT COUNT(*)
                FROM
                """);

        sql.append(config.getTableName());

        sql.append(" WHERE ");

        if (mode == SearchMode.EXACT) {

            appendExactConditions(
                    sql,
                    config
            );

        } else {

            appendFullTextConditions(
                    sql
            );
        }

        appendFilters(
                sql,
                config,
                request
        );

        return sql.toString();
    }

    private void appendExactConditions(
            StringBuilder sql,
            EntitySearchConfig config
    ) {

        for (int i = 0;
             i < config.getSearchFields().size();
             i++) {

            String field =
                    config.getSearchFields().get(i);

            if (i > 0) {
                sql.append(" OR ");
            }

            sql.append(
                    "LOWER(CAST("
                            + field
                            + " AS TEXT)) = LOWER(:query)"
            );
        }
    }

    private void appendFullTextConditions(
            StringBuilder sql
    ) {

        sql.append("""
            search_vector @@
            websearch_to_tsquery(
                'english',
                :query
            )
            """);
    }

    private void appendFilters(
            StringBuilder sql,
            EntitySearchConfig config,
            SearchRequest request
    ) {

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
    }

    private void appendSorting(
            StringBuilder sql,
            EntitySearchConfig config,
            SearchRequest request,
            boolean rankingEnabled
    ) {

        String sortBy =
                request.getSortBy();

        String sortDirection =
                request.getSortDirection();

        if (sortBy != null
                && config.getSortableFields()
                .contains(sortBy)) {

            sql.append(" ORDER BY ");

            sql.append(sortBy);

            if ("DESC".equalsIgnoreCase(
                    sortDirection)) {

                sql.append(" DESC");

            } else {

                sql.append(" ASC");
            }

            return;
        }

        if (rankingEnabled) {

            sql.append("""
                
                ORDER BY score DESC
                """);
        }
    }
}
package com.searchplatform.searchservice.searchframework.repository;

import com.searchplatform.searchservice.searchframework.builder.AutoCompleteQueryBuilder;
import com.searchplatform.searchservice.searchframework.builder.SearchQueryBuilder;
import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import com.searchplatform.searchservice.searchframework.model.AutoCompleteResponse;
import com.searchplatform.searchservice.searchframework.model.SearchRequest;
import com.searchplatform.searchservice.searchframework.model.SearchResponse;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GenericSearchRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private final AutoCompleteQueryBuilder queryBuilder;

    private final SearchQueryBuilder searchQueryBuilder;

    public GenericSearchRepository(
            NamedParameterJdbcTemplate jdbc,
            AutoCompleteQueryBuilder queryBuilder, SearchQueryBuilder searchQueryBuilder
    ) {
        this.jdbc = jdbc;
        this.queryBuilder = queryBuilder;
        this.searchQueryBuilder = searchQueryBuilder;
    }

    public List<AutoCompleteResponse> autoComplete(
            EntitySearchConfig config,
            String query
    ) {

        String sql =
                queryBuilder.build(config);

        return jdbc.query(
                sql,
                new MapSqlParameterSource(
                        "query",
                        query
                ),
                (rs, rowNum) -> {

                    AutoCompleteResponse r =
                            new AutoCompleteResponse();

                    r.setValue(
                            rs.getString("value")
                    );

                    r.setScore(
                            rs.getDouble("score")
                    );

                    return r;
                }
        );
    }

    public SearchResponse search(
            EntitySearchConfig config,
            SearchRequest request
    ) {

        String sql =
                searchQueryBuilder.build(
                        config,
                        request
                );

        MapSqlParameterSource params =
                new MapSqlParameterSource();

        params.addValue(
                "query",
                request.getQuery()
        );

        params.addValue(
                "size",
                request.getSize()
        );

        params.addValue(
                "offset",
                request.getPage()
                        * request.getSize()
        );

        if (request.getFilters() != null) {

            request.getFilters()
                    .forEach(params::addValue);
        }

        List<Map<String, Object>> rows =
                jdbc.queryForList(
                        sql,
                        params
                );
        rows.forEach(row -> {
            row.remove("search_vector");
        });

        SearchResponse response =
                new SearchResponse();

        response.setData(rows);

        response.setPage(
                request.getPage()
        );

        response.setSize(
                request.getSize()
        );

        response.setTotal(
                (long) rows.size()
        );

        return response;
    }
}
package com.searchplatform.searchservice.searchframework.builder;

import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import org.springframework.stereotype.Component;

@Component
public class AutoCompleteQueryBuilder {

    public String build(
            EntitySearchConfig config
    ) {

        String field =
                config.getAutocompleteFields().get(0);

        return """
                SELECT
                    %s AS value,
                    similarity(%s, :query) AS score
                FROM %s
                WHERE
                    %s ILIKE :query || '%%'
                    OR similarity(%s, :query) > 0.2
                ORDER BY score DESC
                LIMIT 10
                """.formatted(
                field,
                field,
                config.getTableName(),
                field,
                field
        );
    }
}
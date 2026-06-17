package com.searchplatform.searchservice.searchframework.builder;

import com.searchplatform.searchservice.searchframework.config.EntitySearchConfig;
import com.searchplatform.searchservice.searchframework.config.SearchMode;
import org.springframework.stereotype.Component;

@Component
public class AutoCompleteQueryBuilder {

    public String build(
            EntitySearchConfig config
    ) {

        SearchMode mode =
                config.getSearch()
                        .getMode();

        if (mode == SearchMode.EXACT) {

            return buildExactQuery(
                    config
            );
        }

        return buildFlexibleQuery(
                config
        );
    }

    private String buildExactQuery(
            EntitySearchConfig config
    ) {

        StringBuilder sql =
                new StringBuilder();

        sql.append("""
            SELECT
            """);

        sql.append(
                config.getAutocomplete()
                        .getFields()
                        .get(0)
        );

        sql.append("""
             AS value,
                1.0 AS score
            FROM
            """);

        sql.append(config.getTableName());

        sql.append("""
             WHERE
            """);

        for (int i = 0;
             i < config.getAutocomplete()
                     .getFields()
                     .size();
             i++) {

            String field =
                    config.getAutocomplete()
                            .getFields()
                            .get(i);

            if (i > 0) {
                sql.append(" OR ");
            }

            sql.append(
                    "LOWER(CAST("
                            + field
                            + " AS TEXT)) = LOWER(:query)"
            );
        }

        sql.append("""
            
            ORDER BY score DESC
            
            LIMIT 10
            """);

        return sql.toString();
    }

    private String buildFlexibleQuery(
            EntitySearchConfig config
    ) {

        boolean prefixEnabled =
                config.getAutocomplete()
                        .getCapabilities()
                        .getPrefix();

        boolean fuzzyEnabled =
                config.getAutocomplete()
                        .getCapabilities()
                        .getFuzzy();

        if (!prefixEnabled
                && !fuzzyEnabled) {

            throw new RuntimeException(
                    "No autocomplete capabilities enabled"
            );
        }

        StringBuilder sql =
                new StringBuilder();

        sql.append("""
        SELECT
        """);

        sql.append(
                config.getAutocomplete()
                        .getFields()
                        .get(0)
        );

        sql.append("""
         AS value,

         GREATEST(
        """);

        boolean firstScore = true;

        for (String field :
                config.getAutocomplete()
                        .getFields()) {

            if (!firstScore) {
                sql.append(", ");
            }

            boolean fuzzyAllowed =
                    !config.getAutocomplete()
                            .getFuzzyExcludedFields()
                            .contains(field);

            sql.append("""
                CASE

                    WHEN LOWER(CAST(
                """);

            sql.append(field);

            sql.append("""
                        AS TEXT
                    )) = LOWER(:query)

                    THEN 1.0

                    WHEN CAST(
                """);

            sql.append(field);

            sql.append("""
                        AS TEXT
                    ) ILIKE :query || '%'

                    THEN 0.9

                    WHEN CAST(
                """);

            sql.append(field);

            sql.append("""
                        AS TEXT
                    ) ILIKE '%' || :query || '%'

                    THEN 0.8
                """);

            if (fuzzyEnabled && fuzzyAllowed) {

                sql.append("""
                    ELSE word_similarity(
                        CAST(
                """);

                sql.append(field);

                sql.append("""
                        AS TEXT
                        ),
                        :query
                    )
                """);

            } else {

                sql.append("""
                    ELSE 0
                """);
            }

            sql.append("""
                END
                """);

            firstScore = false;
        }

        sql.append("""
        ) AS score

        FROM
        """);

        sql.append(config.getTableName());

        sql.append("""
         WHERE
        """);

        boolean firstCondition = true;

        for (String field :
                config.getAutocomplete()
                        .getFields()) {

            boolean fuzzyAllowed =
                    !config.getAutocomplete()
                            .getFuzzyExcludedFields()
                            .contains(field);

            if (!firstCondition) {
                sql.append(" OR ");
            }

            sql.append(
                    "CAST("
                            + field
                            + " AS TEXT) ILIKE '%' || :query || '%'"
            );

            Double threshold =
                    config.getAutocomplete()
                            .getFuzzyThreshold();

            if (fuzzyEnabled && fuzzyAllowed) {

                sql.append(
                        " OR word_similarity(CAST("
                                + field
                                + " AS TEXT), :query) > "
                                + threshold
                );
            }

            firstCondition = false;
        }

        sql.append("""
        
        ORDER BY score DESC
        
        LIMIT 10
        """);

        return sql.toString();
    }
}
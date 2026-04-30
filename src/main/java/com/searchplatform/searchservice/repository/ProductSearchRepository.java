package com.searchplatform.searchservice.repository;

import com.searchplatform.searchservice.model.FacetBucket;
import com.searchplatform.searchservice.model.SearchProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductSearchRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    // 🔹 Common parameter builder
    private Map<String, Object> buildParams(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax,
            int page,
            int size
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("brand", brand);
        params.put("category", category);
        params.put("priceMin", priceMin);
        params.put("priceMax", priceMax);
        params.put("size", size);
        params.put("offset", page * size);
        return params;
    }

    // 🔥 1. Main Search Query
    public List<SearchProductDocument> searchProducts(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax,
            int page,
            int size,
            String orderBy
    ) {

        String sql = """
            SELECT id, name, price, rating, brand_name, category_name,
                   (
                     ts_rank(search_vector, websearch_to_tsquery('english', :query)) * 2
                     + similarity(name, :query) * 1.5
                     + similarity(coalesce(brand_name,''), :query)
                     + similarity(coalesce(category_name,''), :query)
                   ) AS score
            FROM products
            WHERE
                (
                  search_vector @@ websearch_to_tsquery('english', :query)
                  OR similarity(name, :query) > 0.2
                  OR similarity(coalesce(brand_name,''), :query) > 0.2
                  OR similarity(coalesce(category_name,''), :query) > 0.2
                )
                AND (CAST(:brand AS TEXT) IS NULL OR brand_name = CAST(:brand AS TEXT))
                AND (CAST(:category AS TEXT) IS NULL OR category_name = CAST(:category AS TEXT))
                AND (CAST(:priceMin AS NUMERIC) IS NULL OR price >= CAST(:priceMin AS NUMERIC))
                AND (CAST(:priceMax AS NUMERIC) IS NULL OR price <= CAST(:priceMax AS NUMERIC))
                AND is_active = true
        """;

        sql += " ORDER BY " + orderBy + " LIMIT :size OFFSET :offset";

        Map<String, Object> params = buildParams(query, brand, category, priceMin, priceMax, page, size);

        return jdbc.query(sql, params, (rs, rowNum) -> {
            SearchProductDocument p = new SearchProductDocument();
            p.setId(rs.getString("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getDouble("price"));
            p.setRating(rs.getDouble("rating"));
            p.setBrand(rs.getString("brand_name"));
            p.setCategory(rs.getString("category_name"));
            return p;
        });
    }

    // 🔥 2. Brand Facet
    public List<FacetBucket> getBrandFacets(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax
    ) {

        String sql = """
            SELECT brand_name, COUNT(*) AS count
            FROM products
            WHERE
                (
                  search_vector @@ websearch_to_tsquery('english', :query)
                  OR similarity(name, :query) > 0.2
                  OR similarity(coalesce(brand_name,''), :query) > 0.2
                  OR similarity(coalesce(category_name,''), :query) > 0.2
                )
                AND (CAST(:brand AS TEXT) IS NULL OR brand_name = CAST(:brand AS TEXT))
                AND (CAST(:category AS TEXT) IS NULL OR category_name = CAST(:category AS TEXT))
                AND (CAST(:priceMin AS NUMERIC) IS NULL OR price >= CAST(:priceMin AS NUMERIC))
                AND (CAST(:priceMax AS NUMERIC) IS NULL OR price <= CAST(:priceMax AS NUMERIC))
                AND is_active = true
            GROUP BY brand_name
            ORDER BY count DESC
        """;

        Map<String, Object> params = buildParams(query, brand, category, priceMin, priceMax, 0, 0);

        return jdbc.query(sql, params, (rs, rowNum) ->
                new FacetBucket(rs.getString("brand_name"), rs.getLong("count"))
        );
    }

    // 🔥 3. Category Facet
    public List<FacetBucket> getCategoryFacets(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax
    ) {

        String sql = """
            SELECT category_name, COUNT(*) AS count
            FROM products
            WHERE
                (
                  search_vector @@ websearch_to_tsquery('english', :query)
                  OR similarity(name, :query) > 0.2
                  OR similarity(coalesce(brand_name,''), :query) > 0.2
                  OR similarity(coalesce(category_name,''), :query) > 0.2
                )
                AND (CAST(:brand AS TEXT) IS NULL OR brand_name = CAST(:brand AS TEXT))
                AND (CAST(:category AS TEXT) IS NULL OR category_name = CAST(:category AS TEXT))
                AND (CAST(:priceMin AS NUMERIC) IS NULL OR price >= CAST(:priceMin AS NUMERIC))
                AND (CAST(:priceMax AS NUMERIC) IS NULL OR price <= CAST(:priceMax AS NUMERIC))
                AND is_active = true
            GROUP BY category_name
            ORDER BY count DESC
        """;

        Map<String, Object> params = buildParams(query, brand, category, priceMin, priceMax, 0, 0);

        return jdbc.query(sql, params, (rs, rowNum) ->
                new FacetBucket(rs.getString("category_name"), rs.getLong("count"))
        );
    }

    // 🔥 4. Price Facet (Range)
    public List<FacetBucket> getPriceFacets(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax
    ) {

        String sql = """
            SELECT
              CASE
                WHEN price < 10000 THEN '0-10000'
                WHEN price BETWEEN 10000 AND 50000 THEN '10000-50000'
                ELSE '50000+'
              END AS range,
              COUNT(*) AS count
            FROM products
            WHERE
                (
                  search_vector @@ websearch_to_tsquery('english', :query)
                  OR similarity(name, :query) > 0.2
                  OR similarity(coalesce(brand_name,''), :query) > 0.2
                  OR similarity(coalesce(category_name,''), :query) > 0.2
                )
                AND (CAST(:brand AS TEXT) IS NULL OR brand_name = CAST(:brand AS TEXT))
                AND (CAST(:category AS TEXT) IS NULL OR category_name = CAST(:category AS TEXT))
                AND (CAST(:priceMin AS NUMERIC) IS NULL OR price >= CAST(:priceMin AS NUMERIC))
                AND (CAST(:priceMax AS NUMERIC) IS NULL OR price <= CAST(:priceMax AS NUMERIC))
                AND is_active = true
            GROUP BY range
            ORDER BY range
        """;

        Map<String, Object> params = buildParams(query, brand, category, priceMin, priceMax, 0, 0);

        return jdbc.query(sql, params, (rs, rowNum) ->
                new FacetBucket(rs.getString("range"), rs.getLong("count"))
        );
    }

    public long getTotalCount(
            String query,
            String brand,
            String category,
            Double priceMin,
            Double priceMax
    ) {

        String sql = """
        SELECT COUNT(*)
        FROM products
        WHERE
            (
              search_vector @@ websearch_to_tsquery('english', :query)
              OR similarity(name, :query) > 0.2
              OR similarity(coalesce(brand_name,''), :query) > 0.2
              OR similarity(coalesce(category_name,''), :query) > 0.2
            )
            AND (CAST(:brand AS TEXT) IS NULL OR brand_name = CAST(:brand AS TEXT))
            AND (CAST(:category AS TEXT) IS NULL OR category_name = CAST(:category AS TEXT))
            AND (CAST(:priceMin AS NUMERIC) IS NULL OR price >= CAST(:priceMin AS NUMERIC))
            AND (CAST(:priceMax AS NUMERIC) IS NULL OR price <= CAST(:priceMax AS NUMERIC))
            AND is_active = true
    """;

        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("brand", brand);
        params.put("category", category);
        params.put("priceMin", priceMin);
        params.put("priceMax", priceMax);

        return jdbc.queryForObject(sql, params, Long.class);
    }
}

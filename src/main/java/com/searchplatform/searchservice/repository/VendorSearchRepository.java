package com.searchplatform.searchservice.repository;


import com.searchplatform.searchservice.model.AutoCompleteResponse;
import com.searchplatform.searchservice.model.VendorDocument;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VendorSearchRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public VendorSearchRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<AutoCompleteResponse> autoComplete(String query) {

        String sql = """
                SELECT
                    vendor_name,
                    vendor_number,
                    similarity(vendor_name, :query) AS score
                FROM vendor_search
                WHERE
                    vendor_name ILIKE :query || '%'
                    OR similarity(vendor_name, :query) > 0.2
                ORDER BY score DESC
                LIMIT 10
                """;

        return jdbc.query(
                sql,
                new MapSqlParameterSource("query", query),
                (rs, rowNum) -> {
                    AutoCompleteResponse r = new AutoCompleteResponse();
                    r.setVendorName(rs.getString("vendor_name"));
                    r.setVendorNumber(rs.getLong("vendor_number"));
                    r.setScore(rs.getDouble("score"));
                    return r;
                }
        );
    }

    public List<VendorDocument> search(
            String query,
            Integer department,
            int page,
            int size
    ) {

        String sql = """
                SELECT
                    id,
                    vendor_number,
                    vendor_name,
                    department,
                    company_name,
                    currency_name,
                    supplier_status,
                
                    (
                        ts_rank(
                            search_vector,
                            websearch_to_tsquery('english', :query)
                        ) * 2
                
                        + similarity(vendor_name, :query)
                
                    ) AS score
                
                FROM vendor_search
                
                WHERE
                (
                    search_vector @@ websearch_to_tsquery('english', :query)
                    OR similarity(vendor_name, :query) > 0.2
                )
                
                AND (
                    CAST(:department AS INTEGER) IS NULL
                    OR department = :department
                )
                
                ORDER BY score DESC
                
                LIMIT :size OFFSET :offset
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("query", query);
        params.addValue("department", department);
        params.addValue("size", size);
        params.addValue("offset", page * size);

        return jdbc.query(sql, params, (rs, rowNum) -> {

            VendorDocument v = new VendorDocument();

            v.setId(rs.getString("id"));
            v.setVendorNumber(rs.getLong("vendor_number"));
            v.setVendorName(rs.getString("vendor_name"));
            v.setDepartment(rs.getInt("department"));
            v.setCompanyName(rs.getString("company_name"));
            v.setCurrencyName(rs.getString("currency_name"));
            v.setSupplierStatus(rs.getString("supplier_status"));
            v.setScore(rs.getDouble("score"));

            return v;
        });
    }
}


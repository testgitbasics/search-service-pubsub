package com.searchplatform.searchservice.repository;

import com.searchplatform.searchservice.model.LocationGroupResponse;
import com.searchplatform.searchservice.model.LocationResponse;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public LocationRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<LocationGroupResponse> autoComplete(String query) {

        String sql = """
                SELECT
                    id,
                    group_name,
                    similarity(group_name, :query) AS score
                FROM location_group
                WHERE
                    group_name ILIKE :query || '%'
                    OR similarity(group_name, :query) > 0.2
                ORDER BY score DESC
                LIMIT 10
                """;

        return jdbc.query(
                sql,
                new MapSqlParameterSource("query", query),
                (rs, rowNum) -> {

                    LocationGroupResponse r =
                            new LocationGroupResponse();

                    r.setId(rs.getInt("id"));
                    r.setGroupName(rs.getString("group_name"));
                    r.setScore(rs.getDouble("score"));

                    return r;
                }
        );
    }

    public List<LocationResponse> getLocationsByGroup(
            Integer groupId
    ) {

        String sql = """
                SELECT
                    l.id,
                    l.description
                FROM location_group_location_ref r
                JOIN location l
                ON r.location_id = l.id
                WHERE r.group_id = :groupId
                """;

        return jdbc.query(
                sql,
                new MapSqlParameterSource("groupId", groupId),
                (rs, rowNum) -> {

                    LocationResponse r =
                            new LocationResponse();

                    r.setId(rs.getInt("id"));
                    r.setDescription(
                            rs.getString("description")
                    );

                    return r;
                }
        );
    }
}

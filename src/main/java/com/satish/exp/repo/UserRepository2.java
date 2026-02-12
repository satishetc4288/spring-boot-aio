package com.satish.exp.repo;

import com.satish.exp.repo.model.User;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository2 {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository2(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findById(Long id) {
        String sql = """
                    SELECT id, name, email
                    FROM USER_INFO
                    WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(sql, params,
                rs -> rs.next() ? Optional.of(userRowMapper().mapRow(rs, 1)) : Optional.empty());
    }

    public List<User> findByName(String name) {
        String sql = """
                    SELECT id, name, email
                    FROM USER_INFO
                    WHERE name = :name
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("name", name);

        return jdbcTemplate.query(sql, params, userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"));
    }
}

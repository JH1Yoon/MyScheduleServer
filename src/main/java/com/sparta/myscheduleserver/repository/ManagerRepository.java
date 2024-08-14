package com.sparta.myscheduleserver.repository;

import com.sparta.myscheduleserver.entity.Manager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ManagerRepository {
    private final JdbcTemplate jdbcTemplate;

    public ManagerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Manager manager) {
        String sql = "INSERT INTO manager (name, email, created_day, updated_day) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, manager.getName(), manager.getEmail(), manager.getCreatedDay(), manager.getUpdatedDay());
    }

    // 관리자 이름을 ID로 조회
    public String findManagerNameById(Long managerId) {
        String sql = "SELECT name FROM manager WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{managerId}, String.class);
    }

    public Manager findById(Long id) {
        String sql = "SELECT * FROM manager WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<Manager>() {
            @Override
            public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
                Manager manager = new Manager();
                manager.setId(rs.getLong("id"));
                manager.setName(rs.getString("name"));
                manager.setEmail(rs.getString("email"));
                manager.setCreatedDay(rs.getTimestamp("created_day"));
                manager.setUpdatedDay(rs.getTimestamp("updated_day"));
                return manager;
            }
        });
    }

    public List<Manager> findAll() {
        String sql = "SELECT * FROM manager";
        return jdbcTemplate.query(sql, new RowMapper<Manager>() {
            @Override
            public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
                Manager manager = new Manager();
                manager.setId(rs.getLong("id"));
                manager.setName(rs.getString("name"));
                manager.setEmail(rs.getString("email"));
                manager.setCreatedDay(rs.getTimestamp("created_day"));
                manager.setUpdatedDay(rs.getTimestamp("updated_day"));
                return manager;
            }
        });
    }

    public void update(Long id, String name, String email) {
        String sql = "UPDATE manager SET name = ?, email = ?, updated_day = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, email, new Timestamp(System.currentTimeMillis()), id);
    }

    public void delete(Long id) {
    // 존재 여부 확인
        String checkSql = "SELECT COUNT(*) FROM manager WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);
        if (count != null && count > 0) {
            // 존재하면 삭제
            String deleteSql = "DELETE FROM manager WHERE id = ?";
            jdbcTemplate.update(deleteSql, id);
        } else {
            throw new IllegalArgumentException("Manager with id " + id + " not found.");
        }
    }
}
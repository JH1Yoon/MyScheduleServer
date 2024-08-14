package com.sparta.myscheduleserver.repository;

import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MyScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public MyScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 일정 생성
    public MySchedule save(MySchedule mySchedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO schedule (task, manager_id, password, created_day, updated_day) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, mySchedule.getTask());
            preparedStatement.setLong(2, mySchedule.getManagerId()); // 올바른 manager_id 설정
            preparedStatement.setString(3, mySchedule.getPassword());
            preparedStatement.setTimestamp(4, mySchedule.getCreatedDay());
            preparedStatement.setTimestamp(5, mySchedule.getUpdatedDay());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        mySchedule.setId(id);
        return mySchedule;
    }

    // 특정 ID에 해당하는 일정 조회
    public MyScheduleResponseDto getSchedule(Long id) {
        // DB 조회
        MySchedule mySchedule = findById(id);
        if (mySchedule != null) {
            // 관리자의 이름을 조회
            String managerName = jdbcTemplate.queryForObject(
                    "SELECT name FROM manager WHERE id = ?", new Object[]{mySchedule.getManagerId()}, String.class);
            // Entity -> ResponseDto (비밀번호 제외)
            return new MyScheduleResponseDto(mySchedule, managerName);
        } else {
            throw new IllegalArgumentException(id + "라는 ID가 Schedule에 없습니다.");
        }
    }

    // 일정목록 조회
    public List<MyScheduleResponseDto> getSchedules(String updatedDay, String manager, int pageNumber, int pageSize) {
        // SQL 쿼리와 파라미터 리스트 초기화
        StringBuilder sql = new StringBuilder("SELECT s.*, m.name as manager_name FROM schedule s JOIN manager m ON s.manager_id = m.id");
        List<Object> params = new ArrayList<>();

        // 필터 조건 추가
        if (updatedDay != null && !updatedDay.isEmpty()) {
            sql.append(" WHERE DATE(s.updated_day) = ?");
            params.add(Timestamp.valueOf(updatedDay + " 00:00:00"));
        }

        if (manager != null) {
            if (params.isEmpty()) {
                sql.append(" WHERE m.name = ?");
            } else {
                sql.append(" AND m.name = ?");
            }
            params.add(manager);
        }

        // 정렬 추가
        sql.append(" ORDER BY s.updated_day DESC");

        // 페이지네이션 조회
        int offset  = (pageNumber -1) * pageSize;
        sql.append(" LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        // DB 조회
        return jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<MyScheduleResponseDto>() {
            @Override
            public MyScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String task = rs.getString("task");
                Long managerId = rs.getLong("manager_id");
                String managerName = rs.getString("manager_name");
                Timestamp createdDay = rs.getTimestamp("created_day");
                Timestamp updatedDayTimestamp = rs.getTimestamp("updated_day");

                return new MyScheduleResponseDto(id, task, managerId, managerName, createdDay, updatedDayTimestamp);
            }
        });
    }

    // 일정 수정
    public void update(Long id, String task, Long manager_id, Timestamp timestamp) {
        // schedule 내용 수정
        String sql = "UPDATE schedule SET task = ?, manager_id = ?, updated_day = ? WHERE id = ?";
        jdbcTemplate.update(sql, task, manager_id, new Timestamp(System.currentTimeMillis()), id);
    }

    // 일정 삭제
    public void delete(Long id) {
        // schedule 삭제
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // 해당하는 ID의 일정 찾기
    public MySchedule findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                MySchedule mySchedule = new MySchedule();
                mySchedule.setId(resultSet.getLong("id"));
                mySchedule.setTask(resultSet.getString("task"));
                mySchedule.setManagerId(resultSet.getLong("manager_id"));
                mySchedule.setPassword(resultSet.getString("password"));
                mySchedule.setCreatedDay(resultSet.getTimestamp("created_day"));
                mySchedule.setUpdatedDay(resultSet.getTimestamp("updated_day"));
                return mySchedule;
            } else {
                return null;
            }
        }, id);
    }
}

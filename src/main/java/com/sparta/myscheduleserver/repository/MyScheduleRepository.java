package com.sparta.myscheduleserver.repository;

import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public MyScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MySchedule save(MySchedule mySchedule) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (task, manager, password, created_day, updated_day) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, mySchedule.getTask());
                    preparedStatement.setString(2, mySchedule.getManager());
                    preparedStatement.setString(3, mySchedule.getPassword());
                    preparedStatement.setTimestamp(4, mySchedule.getCreatedDay());
                    preparedStatement.setTimestamp(5, mySchedule.getUpdatedDay());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        mySchedule.setId(id);

        return mySchedule;
    }

    public MyScheduleResponseDto getSchedule(Long id) {
        // DB 조회
        MySchedule mySchedule = findById(id);
        if (mySchedule != null) {
            // Entity -> ResponseDto (비밀번호 제외)
            return new MyScheduleResponseDto(
                    mySchedule.getId(),
                    mySchedule.getTask(),
                    mySchedule.getManager(),
                    mySchedule.getCreatedDay(),
                    mySchedule.getUpdatedDay()
            );
        } else {
            throw new IllegalArgumentException(id + "라는 ID가 Schedule에 없습니다.");
        }
    }

    public List<MyScheduleResponseDto> findSchedules(String updatedDay, String manager) {
        // SQL 쿼리와 파라미터 리스트 초기화
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule");
        List<Object> params = new ArrayList<>();

        // 필터 조건 추가
        if (updatedDay != null && !updatedDay.isEmpty()) {
            sql.append(" WHERE DATE(updated_day) = ?");
            params.add(Timestamp.valueOf(updatedDay + " 00:00:00"));
        }

        if (manager != null && !manager.isEmpty()) {
            if (params.isEmpty()) {
                sql.append(" WHERE manager = ?");
            } else {
                sql.append(" AND manager = ?");
            }
            params.add(manager);
        }

        // 정렬 추가
        sql.append(" ORDER BY updated_day DESC");

        // DB 조회
        return jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<MyScheduleResponseDto>() {
            @Override
            public MyScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String task = rs.getString("task");
                String managerName = rs.getString("manager");
                Timestamp createdDay = rs.getTimestamp("created_day");
                Timestamp updatedDayTimestamp = rs.getTimestamp("updated_day");
                return new MyScheduleResponseDto(id, task, managerName, createdDay, updatedDayTimestamp);
            }
        });
    }

    public void update(Long id, String task, String manager, Timestamp timestamp) {
        // schedule 내용 수정
        String sql = "UPDATE schedule SET task = ?, manager = ?, updated_day = ? WHERE id = ?";
        jdbcTemplate.update(sql, task,manager, new Timestamp(System.currentTimeMillis()), id);
    }

    public void delete(Long id) {
        // schedule 삭제
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public MySchedule findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                MySchedule mySchedule = new MySchedule();
                mySchedule.setId(resultSet.getLong("id"));
                mySchedule.setTask(resultSet.getString("task"));
                mySchedule.setManager(resultSet.getString("manager"));
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

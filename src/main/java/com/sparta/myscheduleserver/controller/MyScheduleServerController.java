package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyScheduleServerController {

    private final JdbcTemplate jdbcTemplate;

    public MyScheduleServerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedules")
    public MyScheduleResponseDto createSchedule(@RequestBody MyScheduleRequestDto myScheduleRequestDto) {
        // RequestDto -> Entity
        MySchedule mySchedule = new MySchedule(myScheduleRequestDto);

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

        // Entity -> ResponseDto
        MyScheduleResponseDto myScheduleResponseDto = new MyScheduleResponseDto(mySchedule);

        return myScheduleResponseDto;
    }

    @GetMapping("/schedules")
    public List<MyScheduleResponseDto> getSchedule() {
        // DB 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<MyScheduleResponseDto>() {
            @Override
            public MyScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Schedule 데이터들을 MyScheduleResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String task = rs.getString("task");
                String managerName = rs.getString("manager");
                Timestamp createdDay = rs.getTimestamp("created_day");
                Timestamp updatedDay = rs.getTimestamp("updated_day");
                return new MyScheduleResponseDto(id, task, managerName, createdDay, updatedDay);
            }
        });
    }


}

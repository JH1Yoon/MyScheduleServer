package com.sparta.myscheduleserver.service;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import com.sparta.myscheduleserver.repository.MyScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public MyScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public MyScheduleResponseDto createSchedule(MyScheduleRequestDto myScheduleRequestDto) {
        // RequestDto -> Entity
        MySchedule mySchedule = new MySchedule(myScheduleRequestDto);

        // DB 저장
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);
        MySchedule saveMySchedule = myScheduleRepository.save(mySchedule);

        // Entity -> ResponseDto
        MyScheduleResponseDto myScheduleResponseDto = new MyScheduleResponseDto(mySchedule);

        return myScheduleResponseDto;
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

    public List<MyScheduleResponseDto> getSchedules(String updatedDay, String manager) {
        // DB 저장
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);
        return myScheduleRepository.findSchedules(updatedDay, manager);
    }

    private MySchedule findById(Long id) {
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

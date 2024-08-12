package com.sparta.myscheduleserver.service;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import com.sparta.myscheduleserver.repository.MyScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
}

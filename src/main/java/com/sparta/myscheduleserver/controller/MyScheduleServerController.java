package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.service.MyScheduleService;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class MyScheduleServerController {

    private final JdbcTemplate jdbcTemplate;

    public MyScheduleServerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 일정 생성
    @PostMapping("/schedules")
    public MyScheduleResponseDto createSchedule(@Valid @RequestBody MyScheduleRequestDto myScheduleRequestDto) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.createSchedule(myScheduleRequestDto);
    }

    // 특정 ID에 해당하는 일정 조회
    @GetMapping("/schedules/{id}")
    public MyScheduleResponseDto getSchedule(@PathVariable Long id) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.getSchedule(id);
    }

    // 일정 목록 조회
    @GetMapping("/schedules")
    public List<MyScheduleResponseDto> getSchedules(
            @RequestParam(value = "updatedDay", required = false) String updatedDay,
            @RequestParam(value = "manager", required = false) String manager,
            Integer pageNumber, Integer pageSize) {

        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.getSchedules(updatedDay, manager, pageNumber, pageSize);
    }

    // 일정 수정
    @PutMapping("/schedules/{id}")
    public MyScheduleResponseDto updateSchedule(@PathVariable Long id, @Valid @RequestBody MyScheduleRequestDto myScheduleRequestDto) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.updateSchedule(id, myScheduleRequestDto);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestParam String password) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.deleteSchedule(id, password);
    }
}
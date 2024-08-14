package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.service.MyScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.createSchedule(myScheduleRequestDto);
    }

    @GetMapping("/schedules/{id}")
    public MyScheduleResponseDto getSchedule(@PathVariable Long id) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.getSchedule(id);
    }

    @GetMapping("/schedules")
    public List<MyScheduleResponseDto> getSchedules(
            @RequestParam(value = "updatedDay", required = false) String updatedDay,
            @RequestParam(value = "manager", required = false) String manager,
            Integer pageNumber, Integer pageSize) {

        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.getSchedules(updatedDay, manager, pageNumber, pageSize);
    }

    @PutMapping("/schedules/{id}")
    public MyScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody MyScheduleRequestDto myScheduleRequestDto) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.updateSchedule(id, myScheduleRequestDto);
    }

    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestParam String password) {
        MyScheduleService myScheduleService = new MyScheduleService(jdbcTemplate);
        return myScheduleService.deleteSchedule(id, password);
    }
}
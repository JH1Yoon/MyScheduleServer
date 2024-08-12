package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import com.sparta.myscheduleserver.service.MyScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
            @RequestParam(value = "manager", required = false) String manager) {

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

    @PutMapping("/schedules/{id}")
    public MyScheduleResponseDto  updateMemo(@PathVariable Long id, @RequestBody MyScheduleRequestDto myScheduleRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        MySchedule mySchedule = findById(id);
        if(mySchedule != null) {
            // 비밀번호 확인
            if (!mySchedule.getPassword().equals(myScheduleRequestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // schedule 내용 수정
            String sql = "UPDATE schedule SET task = ?, manager = ?, updated_day = ? WHERE id = ?";
            jdbcTemplate.update(sql, myScheduleRequestDto.getTask(), myScheduleRequestDto.getManager(), new Timestamp(System.currentTimeMillis()), id);

            return new MyScheduleResponseDto(
                    mySchedule.getId(),
                    mySchedule.getTask(),
                    mySchedule.getManager(),
                    mySchedule.getCreatedDay(),
                    mySchedule.getUpdatedDay()
            );
        } else {
            throw new IllegalArgumentException("선택한 Schedule은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedules/{id}")
    public Long deleteMemo(@PathVariable Long id, @RequestParam String password) {
        // 해당 메모가 DB에 존재하는지 확인
        MySchedule mySchedule = findById(id);
        if(mySchedule != null) {
            // 비밀번호 확인
            if (mySchedule.getPassword().equals(password)) {
                // schedule 삭제
                String sql = "DELETE FROM schedule WHERE id = ?";
                jdbcTemplate.update(sql, id);

                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 Schedule은 존재하지 않습니다.");
        }
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

package com.sparta.myscheduleserver.service;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import com.sparta.myscheduleserver.dto.MyScheduleResponseDto;
import com.sparta.myscheduleserver.entity.MySchedule;
import com.sparta.myscheduleserver.repository.ManagerRepository;
import com.sparta.myscheduleserver.repository.MyScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

public class MyScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public MyScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MyScheduleResponseDto createSchedule(MyScheduleRequestDto myScheduleRequestDto) {
        MySchedule mySchedule = new MySchedule(myScheduleRequestDto);

        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);
        MySchedule saveMySchedule = myScheduleRepository.save(mySchedule);

        ManagerRepository managerRepository = new ManagerRepository(jdbcTemplate);
        String managerName = managerRepository.findManagerNameById(saveMySchedule.getManagerId());

        return new MyScheduleResponseDto(saveMySchedule, managerName);
    }

    public MyScheduleResponseDto getSchedule(Long id) {
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);
        return myScheduleRepository.getSchedule(id);
    }

    public List<MyScheduleResponseDto> getSchedules(String updatedDay, String manager) {
        // DB 저장
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);

        return myScheduleRepository.getSchedules(updatedDay, manager);
    }

    public MyScheduleResponseDto updateSchedule(Long id, MyScheduleRequestDto myScheduleRequestDto) {
        // DB 저장
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);
        ManagerRepository managerRepository = new ManagerRepository(jdbcTemplate);
        // 해당 메모가 DB에 존재하는지 확인
        MySchedule mySchedule = myScheduleRepository.findById(id);
        if(mySchedule != null) {
            // 비밀번호 확인
            if (!mySchedule.getPassword().equals(myScheduleRequestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            myScheduleRepository.update(id, myScheduleRequestDto.getTask(), myScheduleRequestDto.getManagerId(), new Timestamp(System.currentTimeMillis()));

        // 업데이트 후, 변경된 스케줄을 조회
            MySchedule updatedSchedule = myScheduleRepository.findById(id);
            String managerName = managerRepository.findManagerNameById(updatedSchedule.getManagerId());
            return new MyScheduleResponseDto(updatedSchedule, managerName);
        } else {
            throw new IllegalArgumentException("선택한 Schedule은 존재하지 않습니다.");
        }
    }

    public Long deleteSchedule(Long id, String password) {
        // DB 저장
        MyScheduleRepository myScheduleRepository = new MyScheduleRepository(jdbcTemplate);

        // 해당 메모가 DB에 존재하는지 확인
        MySchedule mySchedule = myScheduleRepository.findById(id);
        if(mySchedule != null) {
            // 비밀번호 확인
            if (mySchedule.getPassword().equals(password)) {

                myScheduleRepository.delete(id);

                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 Schedule은 존재하지 않습니다.");
        }
    }
}

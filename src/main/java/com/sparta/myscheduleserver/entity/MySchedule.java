package com.sparta.myscheduleserver.entity;

import com.sparta.myscheduleserver.dto.MyScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class MySchedule {
    private Long id;
    private String task;
    private Long managerId;
    private String password;
    private Timestamp createdDay;
    private Timestamp updatedDay;

    public MySchedule(MyScheduleRequestDto myScheduleRequestDto) {
        this.task = myScheduleRequestDto.getTask();
        this.managerId = myScheduleRequestDto.getManagerId();
        this.password = myScheduleRequestDto.getPassword();
        this.createdDay = new Timestamp(System.currentTimeMillis());
        this.updatedDay = createdDay;

    }

    public MySchedule(Long id, String task, Long managerId, String password, Timestamp createdDay, Timestamp updatedDay) {
        this.id = id;
        this.task = task;
        this.managerId = managerId;
        this.password = password;
        this.createdDay = createdDay;
        this.updatedDay = updatedDay;
    }
}
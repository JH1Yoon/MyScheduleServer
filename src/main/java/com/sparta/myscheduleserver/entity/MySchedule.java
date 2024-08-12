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
    private String manager;
    private String password;
    private Timestamp createdDay;
    private Timestamp updatedDay;

    public MySchedule(MyScheduleRequestDto myScheduleRequestDto) {
        this.task = myScheduleRequestDto.getTask();
        this.manager = myScheduleRequestDto.getManager();
        this.password = myScheduleRequestDto.getPassword();
        this.createdDay = new Timestamp(System.currentTimeMillis());
        this.updatedDay = createdDay;

    }
}
package com.sparta.myscheduleserver.dto;

import com.sparta.myscheduleserver.entity.MySchedule;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class MyScheduleResponseDto {
    private Long id;
    private String task;
    private String manager;
    private String password;
    private Timestamp createdDay;
    private Timestamp updatedDay;

    public MyScheduleResponseDto(MySchedule mySchedule) {
        this.id = mySchedule.getId();
        this.task = mySchedule.getTask();
        this.manager = mySchedule.getManager();
        this.createdDay = mySchedule.getCreatedDay();
        this.updatedDay = mySchedule.getUpdatedDay();
    }

    public MyScheduleResponseDto(Long id, String task, String manager, Timestamp createdDay, Timestamp updatedDay) {
        this.id = id;
        this.task = task;
        this.manager = manager;
        this.createdDay = createdDay;
        this.updatedDay = updatedDay;
    }
}
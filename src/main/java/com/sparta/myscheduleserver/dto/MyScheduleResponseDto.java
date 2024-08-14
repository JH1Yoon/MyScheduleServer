package com.sparta.myscheduleserver.dto;

import com.sparta.myscheduleserver.entity.MySchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class MyScheduleResponseDto {
    private Long id;
    private String task;
    private Long managerId;
    private String managerName;
    private Timestamp createdDay;
    private Timestamp updatedDay;

    public MyScheduleResponseDto(Long id, String task, Long managerId, String managerName, Timestamp createdDay, Timestamp updatedDay) {
        this.id = id;
        this.task = task;
        this.managerId = managerId;
        this.managerName = managerName;
        this.createdDay = createdDay;
        this.updatedDay = updatedDay;
    }

    public MyScheduleResponseDto(MySchedule mySchedule, String managerName) {
        this.id = mySchedule.getId();
        this.task = mySchedule.getTask();
        this.managerId = mySchedule.getManagerId();
        this.managerName = managerName;
        this.createdDay = mySchedule.getCreatedDay();
        this.updatedDay = mySchedule.getUpdatedDay();
    }
}
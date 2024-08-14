package com.sparta.myscheduleserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyScheduleRequestDto {
    private String task;
    private Long managerId;
    private String password;

    public MyScheduleRequestDto(String task, Long managerId, String password) {
        this.task = task;
        this.managerId = managerId;
        this.password = password;
    }
}
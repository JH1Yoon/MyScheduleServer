package com.sparta.myscheduleserver.dto;

import lombok.Getter;

@Getter
public class MyScheduleRequestDto {
    private String task;
    private String manager;
    private String password;
}
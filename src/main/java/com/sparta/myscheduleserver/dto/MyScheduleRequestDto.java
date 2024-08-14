package com.sparta.myscheduleserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyScheduleRequestDto {
    @NotBlank(message = "할 일은 필수값")
    @Size(max = 200, message = "할 일은 최대 200자로 입력")
    private String task;

    @NotNull(message = "담당자 ID는 필수값")
    private Long managerId;

    @NotBlank(message = "비밀번호는 필수값")
    private String password;

    public MyScheduleRequestDto(String task, Long managerId, String password) {
        this.task = task;
        this.managerId = managerId;
        this.password = password;
    }
}
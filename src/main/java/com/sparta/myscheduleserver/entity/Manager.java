package com.sparta.myscheduleserver.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Manager {
    private Long id;

    @NotBlank(message = "담당자 이름은 필수값이어야 합니다.")
    private String name;

    @NotBlank
    @Email(message = "email은 이메일 형식이어야합니다.")
    private String email;

    private Timestamp createdDay;
    private Timestamp updatedDay;

    public Manager(Long id, String name, String email, Timestamp createdDay, Timestamp updatedDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdDay = createdDay;
        this.updatedDay = updatedDay;
    }
}

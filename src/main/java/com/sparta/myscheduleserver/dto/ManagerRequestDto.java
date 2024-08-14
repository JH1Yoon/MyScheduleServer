package com.sparta.myscheduleserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagerRequestDto {
    @NotBlank(message = "담당자 이름은 필수값이어야 합니다.")
    private String name;

    @NotBlank(message = "이메일은 필수값이어야 합니다.")
    @Email(message = "이메일은 유효한 형식이어야 합니다.")
    private String email;

    public ManagerRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
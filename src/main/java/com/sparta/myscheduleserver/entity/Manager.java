package com.sparta.myscheduleserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Manager {
    private Long id;
    private String name;
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

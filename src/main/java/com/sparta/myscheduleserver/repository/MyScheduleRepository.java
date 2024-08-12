package com.sparta.myscheduleserver.repository;

import com.sparta.myscheduleserver.entity.MySchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class MyScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public MyScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MySchedule save(MySchedule mySchedule) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (task, manager, password, created_day, updated_day) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, mySchedule.getTask());
                    preparedStatement.setString(2, mySchedule.getManager());
                    preparedStatement.setString(3, mySchedule.getPassword());
                    preparedStatement.setTimestamp(4, mySchedule.getCreatedDay());
                    preparedStatement.setTimestamp(5, mySchedule.getUpdatedDay());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        mySchedule.setId(id);

        return mySchedule;
    }
}

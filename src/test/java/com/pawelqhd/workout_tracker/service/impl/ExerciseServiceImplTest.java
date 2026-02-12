package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.repository.ExerciseRepository;
import com.pawelqhd.workout_tracker.service.ExerciseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ExerciseServiceImplTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private ExerciseService service;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Value("${sql.script.create.exercise}")
    private String sqlCreateExercise;

    @Value("${sql.script.delete.exercise}")
    private String sqlDeleteExercise;

    @BeforeEach
    public void databaseSetup(){
        jdbc.execute(sqlCreateExercise);
    }

    @Test
    public void findExerciseById() {


    }

    @AfterEach
    public void databaseCleanUp(){
        jdbc.execute(sqlDeleteExercise);
    }
}

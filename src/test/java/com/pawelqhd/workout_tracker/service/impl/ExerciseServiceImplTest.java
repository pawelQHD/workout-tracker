package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.entity.Exercise;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ExerciseServiceImplTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private ExerciseService exerciseService;

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
    public void checkIfExerciseIsNull(){

        assertFalse(exerciseRepository.findById(0L).isPresent());
        assertFalse(exerciseService.checkIfNull(0L), "Exercise should be null here");
        assertTrue(exerciseService.checkIfNull(1L), "Exercise should NOT be null here");
    }

    @Test
    public void findExerciseByIdWithValidId() {

        assertTrue(exerciseRepository.findById(1L).isPresent());

        Exercise exercise = exerciseService.findById(1L);

        assertEquals(exerciseRepository.findById(1L).get().getName(),
                exercise.getName(), "Exercise name should match");
    }

    @Test
    public void findExerciseByNameWithValidName(){

        assertTrue(exerciseRepository.findByName("Bench Press").isPresent());

        Exercise exercise = exerciseService.findByName("Bench Press");

        assertEquals(exerciseRepository.findByName("Bench Press").get().getName(),
                exercise.getName(), "Exercise name should match");
    }

    @Test
    public void createExercise(){

        exerciseService.create(new Exercise("Squat"));

        Exercise createdExercise = exerciseRepository.findByName("Squat").get();

        assertEquals("Squat", createdExercise.getName());
    }

    @Test
    public void updateExercise(){

        assertTrue(exerciseRepository.findById(1L).isPresent());
        Exercise updatedExercise = exerciseRepository.findById(1L).get();
        updatedExercise.setName("Dumbbell Bench Press");
        exerciseService.update(1L, updatedExercise);

        assertNotEquals("Bench Press",
                exerciseRepository.findById(1L).get().getName(),
                "Exercise with this name should not exist");
        assertEquals("Dumbbell Bench Press",
                exerciseRepository.findById(1L).get().getName(),
                "Exercise should have been updated to this name");
    }

    @Test
    public void deleteExerciseById(){

        assertTrue(exerciseRepository.findById(1L).isPresent());
        exerciseService.deleteById(1L);
        assertFalse(exerciseRepository.findById(1L).isPresent());
    }

    @Test
    public void getAllExercises(){

        List<Exercise> allExercises = exerciseService.getAll();
        assertEquals(1, allExercises.size());
        exerciseRepository.save(new Exercise("Squat"));
        allExercises = exerciseService.getAll();
        assertEquals(2, allExercises.size());
    }

    @AfterEach
    public void databaseCleanUp(){
        jdbc.execute(sqlDeleteExercise);
    }
}

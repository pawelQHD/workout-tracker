package com.pawelqhd.workout_tracker.dto.workout;

import com.pawelqhd.workout_tracker.dto.workoutexercise.WorkoutExerciseResponseDTO;

import java.util.List;

public class WorkoutResponseDTO {

    private Long id;
    private String name;
    private String notes;
    private List<WorkoutExerciseResponseDTO> exercises;

    public WorkoutResponseDTO() {
    }

    public WorkoutResponseDTO(Long id, String name, String notes, List<WorkoutExerciseResponseDTO> exercises) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.exercises = exercises;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<WorkoutExerciseResponseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseResponseDTO> exercises) {
        this.exercises = exercises;
    }
}

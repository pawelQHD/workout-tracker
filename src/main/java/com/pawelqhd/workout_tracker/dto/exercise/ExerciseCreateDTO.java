package com.pawelqhd.workout_tracker.dto.exercise;

public class ExerciseCreateDTO {

    private String name;

    public ExerciseCreateDTO() {
    }

    public ExerciseCreateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

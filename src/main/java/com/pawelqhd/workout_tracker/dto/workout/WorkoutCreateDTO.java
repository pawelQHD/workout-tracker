package com.pawelqhd.workout_tracker.dto.workout;

public class WorkoutCreateDTO {

    private String name;
    private String notes;

    public WorkoutCreateDTO() {
    }

    public WorkoutCreateDTO(String name, String notes) {
        this.name = name;
        this.notes = notes;
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
}

package com.pawelqhd.workout_tracker.dto.workoutexercise;

import com.pawelqhd.workout_tracker.dto.exercise.ExerciseResponseDTO;

public class WorkoutExerciseResponseDTO {


    private Long id;
    private Long workoutId;
    private ExerciseResponseDTO exercise;
    private int sets;
    private int reps;
    private double weight;
    private int exerciseOrder;

    public WorkoutExerciseResponseDTO() {
    }

    public WorkoutExerciseResponseDTO(Long id, Long workoutId, ExerciseResponseDTO exercise, int sets, int reps, double weight, int exerciseOrder) {
        this.id = id;
        this.workoutId = workoutId;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.exerciseOrder = exerciseOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public ExerciseResponseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseResponseDTO exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getExerciseOrder() {
        return exerciseOrder;
    }

    public void setExerciseOrder(int exerciseOrder) {
        this.exerciseOrder = exerciseOrder;
    }
}

package com.pawelqhd.workout_tracker.dto.workoutexercise;

public class WorkoutExerciseCreateDTO {

    private Long workoutId;
    private Long exerciseId;
    private int sets;
    private int reps;
    private double weight;
    private int exerciseOrder;

    public WorkoutExerciseCreateDTO() {
    }

    public WorkoutExerciseCreateDTO(Long workoutId, Long exerciseId, int sets, int reps, double weight, int exerciseOrder) {
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.exerciseOrder = exerciseOrder;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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

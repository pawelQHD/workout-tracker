package com.pawelqhd.workout_tracker.service;

import com.pawelqhd.workout_tracker.entity.WorkoutExercise;

import java.util.List;

public interface WorkoutExerciseService {

    WorkoutExercise findById(Long id);

    WorkoutExercise create(WorkoutExercise workoutExercise);

    WorkoutExercise update(Long id, WorkoutExercise workoutExercise);

    void deleteById(Long id);

    List<WorkoutExercise> getAll();
}

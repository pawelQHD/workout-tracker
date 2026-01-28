package com.pawelqhd.workout_tracker.service;

import com.pawelqhd.workout_tracker.entity.Exercise;

import java.util.List;

public interface ExerciseService {

    Exercise findById(Long id);

    Exercise create(Exercise exercise);

    Exercise update(Long id, Exercise exercise);

    void deleteById(Long id);

    List<Exercise> getAll();
}

package com.pawelqhd.workout_tracker.service;

import com.pawelqhd.workout_tracker.entity.Workout;

import java.util.List;

public interface WorkoutService {

    Workout findById(Long id);

    Workout create(Workout workout);

    Workout update(Long id, Workout workout);

    void deleteById(Long id);

    List<Workout> getAll();
}

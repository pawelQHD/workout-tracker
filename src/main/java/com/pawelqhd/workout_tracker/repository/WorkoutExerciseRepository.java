package com.pawelqhd.workout_tracker.repository;

import com.pawelqhd.workout_tracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}

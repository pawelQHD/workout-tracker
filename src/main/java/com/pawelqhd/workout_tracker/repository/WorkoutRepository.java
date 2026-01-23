package com.pawelqhd.workout_tracker.repository;

import com.pawelqhd.workout_tracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
}

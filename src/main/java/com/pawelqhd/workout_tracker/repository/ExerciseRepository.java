package com.pawelqhd.workout_tracker.repository;

import com.pawelqhd.workout_tracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    public Optional<Exercise> findByName(String name);
}

package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.entity.WorkoutExercise;
import com.pawelqhd.workout_tracker.repository.WorkoutExerciseRepository;
import com.pawelqhd.workout_tracker.service.WorkoutExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    @Override
    public WorkoutExercise findById(Long id) {

        return workoutExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find WorkoutExercise with id: " + id));
    }

    @Override
    public WorkoutExercise create(WorkoutExercise workoutExercise) {

        WorkoutExercise newWorkoutExercise = new WorkoutExercise();
        newWorkoutExercise.copyEntity(workoutExercise);
        return workoutExerciseRepository.save(newWorkoutExercise);
    }

    @Override
    public WorkoutExercise update(Long id, WorkoutExercise workoutExercise) {

        WorkoutExercise existingWorkoutExercise = this.findById(id);
        existingWorkoutExercise.copyEntity(workoutExercise);
        return workoutExerciseRepository.save(existingWorkoutExercise);
    }

    @Override
    public void deleteById(Long id) {

        workoutExerciseRepository.deleteById(id);
    }

    @Override
    public List<WorkoutExercise> getAll() {

        return workoutExerciseRepository.findAll();
    }
}

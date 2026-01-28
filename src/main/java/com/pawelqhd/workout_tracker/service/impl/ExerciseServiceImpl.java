package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.entity.Exercise;
import com.pawelqhd.workout_tracker.repository.ExerciseRepository;
import com.pawelqhd.workout_tracker.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }


    @Override
    public Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find Exercise with the id: " + id));
    }

    @Override
    public Exercise create(Exercise exercise) {

        Exercise newExercise = new Exercise();

        newExercise.setName(exercise.getName());

        return exerciseRepository.save(newExercise);
    }

    @Override
    public Exercise update(Long id, Exercise exercise) {

        Exercise existingExercise = this.findById(id);

        existingExercise.setName(exercise.getName());

        return exerciseRepository.save(existingExercise);
    }

    @Override
    public void deleteById(Long id) {

        exerciseRepository.deleteById(id);
    }

    @Override
    public List<Exercise> getAll() {

        return exerciseRepository.findAll();
    }
}

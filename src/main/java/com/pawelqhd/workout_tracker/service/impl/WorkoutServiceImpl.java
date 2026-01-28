package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.entity.Workout;
import com.pawelqhd.workout_tracker.repository.WorkoutRepository;
import com.pawelqhd.workout_tracker.service.WorkoutService;

import java.util.List;

public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public Workout findById(Long id) {

        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find Workout with the id: " + id));
    }

    @Override
    public Workout create(Workout workout) {

        Workout newWorkout = new Workout();

        newWorkout.setName(workout.getName());
        newWorkout.setNotes(workout.getNotes());
        newWorkout.setUser(workout.getUser());
        newWorkout.setWorkoutExercises(workout.getWorkoutExercises());

        return workoutRepository.save(newWorkout);
    }

    @Override
    public Workout update(Long id, Workout workout) {

        Workout exisitngWorkout = this.findById(id);

        exisitngWorkout.setName(workout.getName());
        exisitngWorkout.setNotes(workout.getNotes());
        exisitngWorkout.setWorkoutExercises(workout.getWorkoutExercises());

        return workoutRepository.save(exisitngWorkout);
    }

    @Override
    public void deleteById(Long id) {

        workoutRepository.deleteById(id);

    }

    @Override
    public List<Workout> getAll() {

        return workoutRepository.findAll();
    }
}

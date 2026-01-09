package com.pawelqhd.workout_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workouts")
public class WorkoutsRestController {

    @GetMapping("")
    public String getAllWorkouts(){
        return "Workout REST API currently in development";
    }
}

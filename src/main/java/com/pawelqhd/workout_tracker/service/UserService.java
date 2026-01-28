package com.pawelqhd.workout_tracker.service;

import com.pawelqhd.workout_tracker.entity.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    User create(User user);

    User update(Long id, User user);

    void deleteById(Long id);

    List<User> getAll();
}

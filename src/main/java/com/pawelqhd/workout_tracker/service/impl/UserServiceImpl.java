package com.pawelqhd.workout_tracker.service.impl;

import com.pawelqhd.workout_tracker.entity.User;
import com.pawelqhd.workout_tracker.repository.UserRepository;
import com.pawelqhd.workout_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find User with the id: " + id));
    }

    @Override
    public User create(User user) {

        User newUser = new User();
        newUser.copyEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public User update(Long id, User user) {

        User existingUser = this.findById(id);
        existingUser.copyEntity(user);
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteById(Long id) {

        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {

        return userRepository.findAll();
    }
}

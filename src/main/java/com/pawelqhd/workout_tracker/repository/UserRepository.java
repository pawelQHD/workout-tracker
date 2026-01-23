package com.pawelqhd.workout_tracker.repository;

import com.pawelqhd.workout_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

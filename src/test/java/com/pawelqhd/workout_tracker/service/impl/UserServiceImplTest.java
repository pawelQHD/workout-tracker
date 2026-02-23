package com.pawelqhd.workout_tracker.service.impl;


import com.pawelqhd.workout_tracker.entity.User;
import com.pawelqhd.workout_tracker.repository.UserRepository;
import com.pawelqhd.workout_tracker.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @BeforeEach
    public void databaseSetup(){

        User user = new User();
        user.setUserName("John123");
        user.setPassword(passwordEncoder.encode("test123"));
        user.setEmail("johndoe@example.com");
        userRepository.save(user);
    }

    @Test
    public void findUserByIdWithValidId(){

        assertTrue(userRepository.findById(1L).isPresent());
        assertEquals("John123", userService.findById(1L).getUserName(), "Username should match");
    }

    @Test
    public void createUser(){

        User user = new User();
        user.setUserName("Ana123");
        user.setPassword("test123");
        user.setEmail("anasmith@example.com");
        userService.create(user);

        assertTrue(userRepository.findById(2L).isPresent());
        assertFalse(userRepository.findById(3L).isPresent());
    }

    @Test
    public void updateUser(){

        assertTrue(userRepository.findById(1L).isPresent());

        User existingUser = userRepository.findById(1L).get();
        existingUser.setUserName("Ana123");
        existingUser.setEmail("anasmith@example.com");
        userService.update(1L, existingUser);

        assertEquals("Ana123", userRepository.findById(1L).get().getUserName(), "Username should match");
        assertEquals("anasmith@example.com", userRepository.findById(1L).get().getEmail(), "Email should match");

    }

    @Test
    public void deleteUser(){

        assertTrue(userRepository.findById(1L).isPresent());

        userService.deleteById(1L);

        assertFalse(userRepository.findById(1L).isPresent());
    }

    @Test
    public void getAllUsers(){

        assertEquals(1, userService.getAll().size());
        User user = new User();
        user.setUserName("Ana123");
        user.setPassword(passwordEncoder.encode("test123"));
        user.setEmail("anasmith@example.com");
        userRepository.save(user);
        assertEquals(2, userService.getAll().size());

    }

    @Test
    public void isPasswordEncoded(){

        User user = new User();
        user.setUserName("Ana123");
        user.setPassword("test123");
        user.setEmail("anasmith@example.com");
        userService.create(user);

        assertNotEquals("test123", userRepository.findById(2L).get().getPassword(),
                "Password should not be stored as a plain text");

        assertTrue(passwordEncoder.matches("test123", userRepository.findById(2L).get().getPassword()),
                "Passwords should match correctly");
    }

    @AfterEach
    public void databaseCleanUp(){
        jdbc.execute(sqlDeleteUser);
    }
}

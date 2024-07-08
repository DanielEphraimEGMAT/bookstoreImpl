package com.sample.bookstore1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sample.bookstore1.model.User;
import com.sample.bookstore1.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}

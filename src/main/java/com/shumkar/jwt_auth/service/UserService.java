package com.shumkar.jwt_auth.service;

import com.shumkar.jwt_auth.model.User;
import com.shumkar.jwt_auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);

        return users;
    }
}

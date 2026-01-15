package com.shumkar.jwt_auth.controller;

import com.shumkar.jwt_auth.model.User;
import com.shumkar.jwt_auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = (User) authentication.getPrincipal();

        return currentUser != null
                ? ResponseEntity.ok(currentUser)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = this.userService.allUsers();
        return ResponseEntity.ok(users);
    }
}

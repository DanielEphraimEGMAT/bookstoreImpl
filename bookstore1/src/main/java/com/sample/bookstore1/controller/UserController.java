package com.sample.bookstore1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.bookstore1.model.User;
import com.sample.bookstore1.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Related API's", description = "Provides API's to fetch Current / All users")
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Fetch Current User", description = "Fetch the logged in user", tags = { "loggedInUser",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserController.class), mediaType = "application/json") })
    })
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @Operation(summary = "Fetch All Users", description = "Fetch all users in the database", tags = { "allUser",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserController.class), mediaType = "application/json") })
    })
    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}
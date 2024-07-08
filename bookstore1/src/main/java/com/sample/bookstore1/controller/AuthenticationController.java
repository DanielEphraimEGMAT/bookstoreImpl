package com.sample.bookstore1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.bookstore1.dto.LoginResponse;
import com.sample.bookstore1.dto.LoginUserDto;
import com.sample.bookstore1.dto.RegisterUserDto;
import com.sample.bookstore1.model.User;
import com.sample.bookstore1.service.AuthenticationService;
import com.sample.bookstore1.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication API's", description = "Provides API's for Login / Signup")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Sign Up User", description = "Sign Up User by providing email, password and full name.", tags = {
            "signup", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json") })
    })
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Sign In User", description = "Sign Up User by providing email and password", tags = {
            "signin", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json") })
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

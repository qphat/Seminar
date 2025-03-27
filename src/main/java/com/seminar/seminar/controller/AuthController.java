package com.seminar.seminar.controller;

import com.seminar.seminar.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.seminar.seminar.request.LoginRequest;
import com.seminar.seminar.request.SignupRequest;
import com.seminar.seminar.response.AuthResponse;
import com.seminar.seminar.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth") // Thêm prefix cho URL
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody SignupRequest req) throws Exception {

        String token = authService.createUser(req);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setRole(Role.DELEGATE);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid  @RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.signin(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}

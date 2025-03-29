package com.seminar.seminar.service;

import com.seminar.seminar.request.LoginRequest;
import com.seminar.seminar.request.SignupRequest;
import com.seminar.seminar.response.AuthResponse;

public interface AuthService {

    String createUser(SignupRequest req) throws Exception;
    AuthResponse signin(LoginRequest req);
}

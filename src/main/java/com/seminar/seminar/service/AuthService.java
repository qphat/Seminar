package service;

import request.LoginRequest;
import request.SignupRequest;
import response.AuthResponse;

public interface AuthService {

    String createUser(SignupRequest req) throws Exception;
    AuthResponse signin(LoginRequest req);
}

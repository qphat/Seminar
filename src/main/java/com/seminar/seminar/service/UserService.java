package com.seminar.seminar.service;

import com.seminar.seminar.exception.UserException;
import com.seminar.seminar.model.User;

public interface UserService {
    User findUserProfileByJwt(String jwt) throws UserException;
    User findUserByEmail(String email) throws UserException;

}

package service;

import exception.UserException;
import model.User;

public interface UserService {
    User findUserProfileByJwt(String jwt) throws UserException;
    User findUserByEmail(String email) throws UserException;

}

package com.seminar.seminar.service.Imp;


import com.seminar.seminar.config.JwtProvider;
import com.seminar.seminar.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.seminar.seminar.model.User;
import org.springframework.stereotype.Service;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.service.UserService;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserServicesImp implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email=jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user==null) {
            throw new UserException("user not exist with email "+email);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        User user=userRepository.findByEmail(email);
        if(user!=null) {
            return user;
        }
        throw new UserException("user not exist with email "+email);
    }
}

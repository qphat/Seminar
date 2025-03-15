package service.Imp;

import config.JwtProvider;
import exception.UserException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import model.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserServicesImp implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
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

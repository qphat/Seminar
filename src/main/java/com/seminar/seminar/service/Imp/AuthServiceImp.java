//package service.Imp;
//
//import config.JwtProvider;
//import domain.Role;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import model.User;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import repository.UserRepository;
//import request.LoginRequest;
//import request.SignupRequest;
//import response.AuthResponse;
//import service.AuthService;
//import service.UserService;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImp implements AuthService {
//
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//    private final JwtProvider jwtProvider;
//    private final CustomUserServiceImplementation customUserDetails;
//
//
//    @Override
//    public String createUser(SignupRequest req) throws Exception {
//
//        String email = req.getEmail();
//        String fullName = req.getFullName();
//
//        User user = userRepository.findByEmail(email);
//
//        if (user == null) {
//            User createdUser = new User();
//            createdUser.setEmail(email);
//            createdUser.setFullName(fullName);
//            createdUser.setRole(Role.DELEGATE);
//            createdUser.setPassword(passwordEncoder.encode("defaultPassword")); // Thay OTP bằng mật khẩu mặc định
//
//            System.out.println(createdUser);
//
//            user = userRepository.save(createdUser);
//        }
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(
//                Role.DELEGATE.toString()));
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                email, null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        return jwtProvider.generateToken(authentication);
//    }
//
//    @Override
//    public AuthResponse signin(LoginRequest req) {
//        String username = req.getEmail();
//        String password = req.getPassword(); // Thay otp bằng password
//
//        System.out.println(username + " ----- " + password);
//
//        Authentication authentication = authenticate(username, password);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtProvider.generateToken(authentication);
//        AuthResponse authResponse = new AuthResponse();
//
//        authResponse.setMessage("Login Success");
//        authResponse.setJwt(token);
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
//
//        authResponse.setRole(Role.valueOf(roleName));
//
//        return authResponse;
//    }
//
//    private Authentication authenticate(String username, String password){
//        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
//
//        System.out.println("sign in userDetails - " + userDetails);
//
//        if (userDetails == null) {
//            throw new UsernameNotFoundException("User not found: " + username);
//        }
//
//        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid password for user: " + username);
//        }
//
//
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }
//
////}
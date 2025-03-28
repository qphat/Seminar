package com.seminar.seminar.service.Imp;

import com.seminar.seminar.config.JwtProvider;
import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.request.LoginRequest;
import com.seminar.seminar.request.SignupRequest;
import com.seminar.seminar.response.AuthResponse;
import com.seminar.seminar.service.AuthService;
import com.seminar.seminar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final CustomeUserServiceImplementation customUserDetails;


    @Override
    public String createUser(SignupRequest req) throws Exception {
        String email = req.getEmail();
        String fullName = req.getFullName();
        String password = req.getPassword();
        String phone = req.getPhone();

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered: " + email);
        }

        // Tạo user mới
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setRole(Role.DELEGATE);
        createdUser.setPhone(phone);
        createdUser.setPassword(passwordEncoder.encode(password));

        // Lưu user vào database
        userRepository.save(createdUser);

        // Tạo danh sách quyền hạn
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.DELEGATE.toString()));

        // Xác thực user ngay sau khi đăng ký
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về token
        return jwtProvider.generateToken(authentication);
    }


    @Override
    public AuthResponse signin(LoginRequest req) {
        String username = req.getEmail();
        String password = req.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        User user = userRepository.findByEmail(username);
        authResponse.setFullName(user.getFullName());
        authResponse.setId(user.getId());
        authResponse.setRole(Role.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password for user: " + username);
        }


        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

//}
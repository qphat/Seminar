package service.Imp;

import domain.Role;
import model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomeUserServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomeUserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }

        throw new UsernameNotFoundException("User not found with email - " + username);
    }

    private UserDetails buildUserDetails(String email, String password, Role role) {
        if (role == null) role = Role.DELEGATE;

        Set<GrantedAuthority> authorities = new HashSet<>(Set.of(new SimpleGrantedAuthority(role.toString())));

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}

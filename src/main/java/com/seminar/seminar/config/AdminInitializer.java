package com.seminar.seminar.config;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    @Value("${admin.default.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.default.password}")

    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail(adminEmail);
            admin.setRole(Role.ADMIN);
//                  b
            admin.setPassword(passwordEncoder.encode(adminPassword));
            userRepository.save(admin);

            logger.info("Admin account created: {} / {}", adminEmail, adminPassword);
        } else {
            logger.info("Admin account already exists: {}", adminEmail);
        }
    }
}



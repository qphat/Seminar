package com.seminar.seminar.response;

import com.seminar.seminar.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String jwt;

    private String message;

    private Role role;
}


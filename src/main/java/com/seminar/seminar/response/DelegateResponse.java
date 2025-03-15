package com.seminar.seminar.response;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import lombok.Data;

@Data
public class DelegateResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;

    public DelegateResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}

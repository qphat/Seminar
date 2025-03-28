package com.seminar.seminar.response;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DelegateResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String phone;


    public DelegateResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phone = user.getPhone();
    }
}

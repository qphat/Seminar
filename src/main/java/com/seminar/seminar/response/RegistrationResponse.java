package com.seminar.seminar.response;

import com.seminar.seminar.domain.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
    private Long delegateId;
    private String fullName;
    private String email;
    private RegistrationStatus status;
}

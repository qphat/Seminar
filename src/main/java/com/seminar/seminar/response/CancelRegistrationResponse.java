package com.seminar.seminar.response;

import com.seminar.seminar.domain.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelRegistrationResponse {
    private Long delegateId;
    private Long conferenceId;
    private RegistrationStatus status;
    private String message;
}


package com.seminar.seminar.service;

import com.seminar.seminar.response.RegistrationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RegistrationService {
    String registerForConference(Long delegateId, Long conferenceId);
    String cancelRegistration(Long id);
    List<RegistrationResponse> getRegisteredDelegates(Long conferenceId);
}


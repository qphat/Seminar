package com.seminar.seminar.controller;

import com.seminar.seminar.request.RegistrationRequest;
import com.seminar.seminar.response.*;
import com.seminar.seminar.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping
    public ResponseEntity<List<RegistrationDetailsResponse>> getAllRegistrations() {
        List<RegistrationDetailsResponse> registrations = registrationService.getAllRegistrations();
        return ResponseEntity.ok(registrations);
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> registerForConference(@RequestBody RegistrationRequest request) {
        log.info("Received request: {}", request);

        RegistrationResponse response = registrationService.registerForConference(
                request.getDelegateId(),
                request.getConferenceId()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/cancel")
    public ResponseEntity<CancelRegistrationResponse> cancelRegistration(@RequestParam Long delegateId, @RequestParam Long conferenceId) {
        CancelRegistrationResponse response = registrationService.cancelRegistration(delegateId, conferenceId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/conferences/{id}")
    public ResponseEntity<List<RegistrationResponse>> getRegisteredDelegates(@PathVariable Long id) {
        List<RegistrationResponse> registrations = registrationService.getRegisteredDelegates(id);
        return ResponseEntity.ok(registrations);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @GetMapping("/delegate/{id}")
    public ResponseEntity<List<ListRegistrationsResponse>> getRegistrationsByDelegate(@PathVariable Long id) {
        List<ListRegistrationsResponse> registrations = registrationService.getConferencesByDelegate(id);
        return ResponseEntity.ok(registrations);
    }

    @PutMapping("/update-status")
    public ResponseEntity<UpdateStatusResponse> updateRegistrationStatus(
            @RequestParam Long delegateId,
            @RequestParam Long conferenceId,
            @RequestParam String newStatus) {
        UpdateStatusResponse response = registrationService.updateRegistrationStatus(delegateId, conferenceId, newStatus);
        if (response.getStatus().equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
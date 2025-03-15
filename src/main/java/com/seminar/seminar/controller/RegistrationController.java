package com.seminar.seminar.controller;

import com.seminar.seminar.response.RegistrationResponse;
import com.seminar.seminar.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> registerForConference(
            @RequestParam Long delegateId,
            @RequestParam Long conferenceId) {
        String message = registrationService.registerForConference(delegateId, conferenceId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long id) {
        String message = registrationService.cancelRegistration(id);
        return ResponseEntity.ok(message);
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
}

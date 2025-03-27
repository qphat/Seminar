package com.seminar.seminar.controller;

import com.seminar.seminar.model.Conference;
import com.seminar.seminar.response.ConferenceResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping
    public ResponseEntity<String> createConference(@RequestBody Conference conference ) {
        String message = conferenceService.createConference(conference);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<ConferenceResponse>> getAllConferences() {
        List<ConferenceResponse> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConferenceResponse> getConferenceById(@PathVariable Long id) {
        ConferenceResponse response = conferenceService.getConferenceById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConference(@PathVariable Long id, @RequestBody Conference conference) {
        String message = conferenceService.updateConference(id, conference);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteConference(@PathVariable Long id) {
        DeleteResponse response = conferenceService.deleteConference(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
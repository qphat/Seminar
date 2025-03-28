package com.seminar.seminar.controller;

import com.seminar.seminar.model.Conference;
import com.seminar.seminar.response.ConferenceResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.response.StatusResponse;
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
    public ResponseEntity<StatusResponse> createConference(@RequestBody Conference conference) {
        StatusResponse response = conferenceService.createConference(conference);

        if ("error".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
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
    public StatusResponse updateConference(@PathVariable Long id, @RequestBody Conference conference) {
        try {
            return conferenceService.updateConference(id, conference);
        } catch (IllegalArgumentException e) {
            return new StatusResponse("error", e.getMessage());
        }
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
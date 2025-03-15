package com.seminar.seminar.response;

import com.seminar.seminar.model.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ConferenceResponse {
    private Long id;
    private String title;
    private LocalDate date;
    private String location;
    private Integer capacity;
    private int registeredDelegates;
}
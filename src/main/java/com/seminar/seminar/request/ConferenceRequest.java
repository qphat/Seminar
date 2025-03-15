package com.seminar.seminar.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConferenceRequest {
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private Integer capacity;
}
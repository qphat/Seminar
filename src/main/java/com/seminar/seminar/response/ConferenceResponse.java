package com.seminar.seminar.response;

import com.seminar.seminar.model.Conference;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ConferenceResponse {
    private Long id;
    private String title;
    private LocalDate date;
    private String location;
    private Integer capacity;
    private int registeredDelegates;

    public ConferenceResponse(Long id, String title, LocalDate date, String location, Integer capacity, int registeredDelegates) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.registeredDelegates = registeredDelegates;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public int getRegisteredDelegates() {
        return registeredDelegates;
    }
}
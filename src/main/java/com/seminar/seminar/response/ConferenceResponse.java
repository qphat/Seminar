package com.seminar.seminar.response;

import com.seminar.seminar.domain.ConferenceStatus;
import com.seminar.seminar.domain.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConferenceResponse {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer capacity;
    private int registeredDelegates;
    private ConferenceStatus status;
    private LocalDateTime registrationDeadline;


}
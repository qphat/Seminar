package com.seminar.seminar.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConferenceReport {
    private Long conferenceId;
    private String title;
    private int totalDelegates;
}

package com.seminar.seminar.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DelegateReport {
    private Long delegateId;
    private String fullName;
    private int totalConferences;
}

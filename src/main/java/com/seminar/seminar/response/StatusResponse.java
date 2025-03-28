package com.seminar.seminar.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse {
    private String status; // "success" hoặc "error"
    private String message;
}

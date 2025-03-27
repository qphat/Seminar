package com.seminar.seminar.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ListRegistrationsResponse {
    private Long id; // ID của hội nghị
    private String title; // Tiêu đề hội nghị
    private LocalDateTime startDate; // Ngày bắt đầu
    private LocalDateTime endDate; // Ngày kết thúc
    private String location; // Địa điểm
    private int capacity; // Sức chứa
    private int registeredDelegates; // Số lượng đại biểu đã đăng ký
    private String status; // Trạng thái của bản ghi đăng ký (từ Registration)
    private LocalDateTime registrationDeadline; // Hạn chót đăng ký
}

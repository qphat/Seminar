package com.seminar.seminar.controller;

import com.seminar.seminar.response.ConferenceReport;
import com.seminar.seminar.response.DelegateReport;
import com.seminar.seminar.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {


    private final ReportService reportService;

    /**
     * 6.1 Thống kê số lượng đại biểu tham gia hội thảo
     * @return List<ConferenceReport>
     */
    @GetMapping("/conferences")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ConferenceReport>> getConferenceStats() {
        List<ConferenceReport> stats = reportService.getConferenceStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 6.2 Thống kê số lượng hội thảo mà một đại biểu đã tham gia
     * @param id ID của đại biểu
     * @return DelegateReport
     */
    @GetMapping("/delegates/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DelegateReport> getDelegateStats(@PathVariable Long id) {
        DelegateReport stats = reportService.getDelegateStats(id);
        return ResponseEntity.ok(stats);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

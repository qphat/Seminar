package com.seminar.seminar.controller;

import com.seminar.seminar.response.NotificationResponse;
import com.seminar.seminar.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;

    /**
     * 5.1 Gửi thông báo đến đại biểu
     * @param request Body chứa delegateId và message
     * @return ResponseEntity với thông báo
     */
    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> request) {
        Long delegateId = Long.valueOf(request.get("delegateId").toString());
        String message = request.get("message").toString();
        String result = notificationService.sendNotification(delegateId, message);
        return ResponseEntity.ok(result);
    }

    /**
     * 5.2 Lấy danh sách thông báo của đại biểu
     * @param id ID của đại biểu
     * @return List<NotificationResponse>
     */
    @GetMapping("/delegates/{id}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByDelegate(@PathVariable Long id) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByDelegate(id);
        return ResponseEntity.ok(notifications);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
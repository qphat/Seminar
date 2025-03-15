package com.seminar.seminar.service;

import com.seminar.seminar.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    String sendNotification(Long delegateId, String message);
    List<NotificationResponse> getNotificationsByDelegate(Long delegateId);
}

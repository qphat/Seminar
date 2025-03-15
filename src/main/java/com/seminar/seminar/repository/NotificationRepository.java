package com.seminar.seminar.repository;

import com.seminar.seminar.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDelegateId(Long delegateId);
}

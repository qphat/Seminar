package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.Notification;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.NotificationRepository;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.NotificationResponse;
import com.seminar.seminar.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public String sendNotification(Long delegateId, String message) {
        Optional<User> delegateOpt = userRepository.findById(delegateId);
        if (delegateOpt.isEmpty()) {
            throw new IllegalArgumentException("Delegate not found with id: " + delegateId);
        }

        User delegate = delegateOpt.get();
        if (delegate.getRole() != Role.DELEGATE) {
            throw new IllegalArgumentException("User with id " + delegateId + " is not a delegate");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        Notification notification = new Notification();
        notification.setDelegate(delegate);
        notification.setMessage(message);
        notificationRepository.save(notification);

        return "Notification sent successfully";
    }

    /**
     * 5.2 Lấy danh sách thông báo của đại biểu
     * @param delegateId ID của đại biểu
     * @return List<NotificationResponse>
     * @throws IllegalArgumentException nếu không tìm thấy đại biểu hoặc không phải ROLE_DELEGATE
     */
    public List<NotificationResponse> getNotificationsByDelegate(Long delegateId) {
        Optional<User> delegateOpt = userRepository.findById(delegateId);
        if (delegateOpt.isEmpty()) {
            throw new IllegalArgumentException("Delegate not found with id: " + delegateId);
        }

        User delegate = delegateOpt.get();
        if (delegate.getRole() != Role.DELEGATE) {
            throw new IllegalArgumentException("User with id " + delegateId + " is not a delegate");
        }

        List<Notification> notifications = notificationRepository.findByDelegateId(delegateId);
        return notifications.stream()
                .map(n -> new NotificationResponse(n.getId(), n.getMessage(), n.getSentAt()))
                .collect(Collectors.toList());
    }
}


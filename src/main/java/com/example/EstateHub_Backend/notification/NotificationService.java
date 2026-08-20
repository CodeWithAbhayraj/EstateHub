package com.example.EstateHub_Backend.notification;

import com.example.EstateHub_Backend.notification.dto.NotificationResponse;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    // =====================================================
    // CREATE NOTIFICATION
    // =====================================================

    @Transactional
    public void createNotification(
            Long userId,
            NotificationType type,
            String message,
            Long referenceId,
            String referenceType
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .message(message)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }


    // =====================================================
    // GET MY NOTIFICATIONS
    // =====================================================

    public List<NotificationResponse> getMyNotifications(
            String email
    ) {

        User user = getUserByEmail(email);

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET UNREAD NOTIFICATIONS
    // =====================================================

    public List<NotificationResponse> getUnreadNotifications(
            String email
    ) {

        User user = getUserByEmail(email);

        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc(
                        user.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // UNREAD COUNT
    // =====================================================

    public long getUnreadCount(String email) {

        User user = getUserByEmail(email);

        return notificationRepository
                .countByUserIdAndReadFalse(user.getId());
    }


    // =====================================================
    // MARK AS READ
    // =====================================================

    @Transactional
    public void markAsRead(
            Long notificationId,
            String email
    ) {

        User user = getUserByEmail(email);

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        if (!notification.getUser().getId()
                .equals(user.getId())) {

            throw new AccessDeniedException(
                    "You can update only your own notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }


    // =====================================================
    // MARK ALL AS READ
    // =====================================================

    @Transactional
    public void markAllAsRead(String email) {

        User user = getUserByEmail(email);

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadFalseOrderByCreatedAtDesc(
                                user.getId()
                        );

        notifications.forEach(
                notification -> notification.setRead(true)
        );

        notificationRepository.saveAll(notifications);
    }


    // =====================================================
    // PRIVATE METHODS
    // =====================================================

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }


    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .referenceId(notification.getReferenceId())
                .referenceType(notification.getReferenceType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
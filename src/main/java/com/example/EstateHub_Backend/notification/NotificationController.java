package com.example.EstateHub_Backend.notification;

import com.example.EstateHub_Backend.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    // =====================================================
    // GET ALL MY NOTIFICATIONS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
    getMyNotifications(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                notificationService.getMyNotifications(
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // GET UNREAD NOTIFICATIONS
    // =====================================================

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // UNREAD COUNT
    // =====================================================

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // MARK ONE AS READ
    // =====================================================

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            Authentication authentication
    ) {

        notificationService.markAsRead(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }


    // =====================================================
    // MARK ALL AS READ
    // =====================================================

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            Authentication authentication
    ) {

        notificationService.markAllAsRead(
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
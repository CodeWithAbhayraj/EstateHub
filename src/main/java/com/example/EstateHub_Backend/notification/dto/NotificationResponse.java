package com.example.EstateHub_Backend.notification.dto;

import com.example.EstateHub_Backend.notification.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long id;

    private NotificationType type;

    private String message;

    private Long referenceId;

    private String referenceType;

    private Boolean read;

    private LocalDateTime createdAt;
}
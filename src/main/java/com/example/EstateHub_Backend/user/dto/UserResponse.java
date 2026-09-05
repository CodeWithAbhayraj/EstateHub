package com.example.EstateHub_Backend.user.dto;

import com.example.EstateHub_Backend.user.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String mobile;

    private Role role;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
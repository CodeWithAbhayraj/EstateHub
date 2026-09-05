package com.example.EstateHub_Backend.user;

import com.example.EstateHub_Backend.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    // ==========================================
    // GET ALL USERS
    // ==========================================

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        return mapToResponse(user);
    }


    // ==========================================
    // ENABLE USER
    // ==========================================

    @Transactional
    public UserResponse enableUser(Long id) {

        User user = getUser(id);

        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }


    // ==========================================
    // DISABLE USER
    // ==========================================

    @Transactional
    public UserResponse disableUser(Long id) {

        User user = getUser(id);

        // Prevent disabling SUPER_ADMIN
        if (user.getRole() == Role.SUPER_ADMIN) {

            throw new AccessDeniedException(
                    "SUPER_ADMIN cannot be disabled"
            );
        }

        user.setEnabled(false);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }


    // ==========================================
    // PRIVATE - GET USER
    // ==========================================

    private User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );
    }


    // ==========================================
    // ENTITY → RESPONSE
    // ==========================================

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()

                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())

                .build();
    }
}
package com.example.EstateHub_Backend.user;

import com.example.EstateHub_Backend.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // ==========================================
    // ADMIN - GET ALL USERS
    // ==========================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }


    // ==========================================
    // ADMIN - GET USER BY ID
    // ==========================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }


    // ==========================================
    // ADMIN - ENABLE USER
    // ==========================================

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<UserResponse> enableUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.enableUser(id)
        );
    }


    // ==========================================
    // ADMIN - DISABLE USER
    // ==========================================

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<UserResponse> disableUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.disableUser(id)
        );
    }
}
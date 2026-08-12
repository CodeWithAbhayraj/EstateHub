package com.example.EstateHub_Backend.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class SecurityTestController {

    @GetMapping("/public")
    public String publicApi() {
        return "Public API working";
    }

    @GetMapping("/buyer")
    @PreAuthorize("hasRole('BUYER')")
    public String buyerApi() {
        return "Buyer access granted";
    }

    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public String sellerApi() {
        return "Seller access granted";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Admin access granted";
    }

    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String superAdminApi() {
        return "Super Admin access granted";
    }

    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String adminDashboard() {
        return "Admin dashboard access granted";
    }
}
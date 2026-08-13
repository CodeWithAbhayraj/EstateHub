package com.example.EstateHub_Backend.lead;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    // Create Lead
    @PostMapping
    public ResponseEntity<Lead> createLead(
            @RequestParam Long buyerId,
            @RequestParam Long propertyId,
            @RequestParam String budget,
            @RequestParam LocalDate preferredVisitDate,
            @RequestParam String message
    ) {

        Lead lead = leadService.createLead(
                buyerId,
                propertyId,
                budget,
                preferredVisitDate,
                message
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lead);
    }

    // Admin - Get all leads
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {

        return ResponseEntity.ok(
                leadService.getAllLeads()
        );
    }

    // Get Lead by ID
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leadService.getLeadById(id)
        );
    }
}
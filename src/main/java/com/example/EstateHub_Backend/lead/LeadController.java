package com.example.EstateHub_Backend.lead;

import com.example.EstateHub_Backend.lead.dto.LeadRequest;
import com.example.EstateHub_Backend.lead.dto.LeadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    // Buyer - Create Lead
    @PostMapping
    public ResponseEntity<LeadResponse> createLead(
            @Valid @RequestBody LeadRequest request
    ) {

        LeadResponse response = leadService.createLead(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Admin - Get all leads
    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAllLeads() {

        return ResponseEntity.ok(
                leadService.getAllLeads()
        );
    }

    // Admin - Get Lead by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getLeadById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                leadService.getLeadById(id)
        );
    }
}
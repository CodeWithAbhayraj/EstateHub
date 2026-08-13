package com.example.EstateHub_Backend.lead;

import com.example.EstateHub_Backend.lead.dto.LeadRequest;
import com.example.EstateHub_Backend.lead.dto.LeadResponse;
import com.example.EstateHub_Backend.property.Property;
import com.example.EstateHub_Backend.property.PropertyRepository;
import com.example.EstateHub_Backend.property.PropertyStatus;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    // ===============================
    // CREATE LEAD
    // ===============================

    public LeadResponse createLead(LeadRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Buyer not found")
                );

        if (buyer.getRole() !=
                com.example.EstateHub_Backend.user.Role.BUYER) {

            throw new RuntimeException(
                    "Only buyers can create leads"
            );
        }

        Property property = propertyRepository
                .findById(request.getPropertyId())
                .orElseThrow(() ->
                        new RuntimeException("Property not found")
                );

        if (property.getStatus() != PropertyStatus.PUBLISHED) {

            throw new RuntimeException(
                    "Lead can only be created for published property"
            );
        }

        Lead lead = Lead.builder()
                .buyer(buyer)
                .property(property)
                .budget(request.getBudget())
                .preferredVisitDate(
                        request.getPreferredVisitDate()
                )
                .message(request.getMessage())
                .status(LeadStatus.NEW)
                .build();

        Lead savedLead = leadRepository.save(lead);

        return mapToResponse(savedLead);
    }

    // ===============================
    // ADMIN - GET ALL LEADS
    // ===============================

    public List<LeadResponse> getAllLeads() {

        return leadRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===============================
    // GET LEAD BY ID
    // ===============================

    public LeadResponse getLeadById(Long id) {

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found")
                );

        return mapToResponse(lead);
    }

    // ===============================
    // ENTITY → RESPONSE
    // ===============================

    private LeadResponse mapToResponse(Lead lead) {

        return LeadResponse.builder()
                .id(lead.getId())
                .propertyId(lead.getProperty().getId())
                .propertyTitle(lead.getProperty().getTitle())
                .budget(lead.getBudget())
                .preferredVisitDate(
                        lead.getPreferredVisitDate()
                )
                .message(lead.getMessage())
                .status(lead.getStatus())
                .remarks(lead.getRemarks())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
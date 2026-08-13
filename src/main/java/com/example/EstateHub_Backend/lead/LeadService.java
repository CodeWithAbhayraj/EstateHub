package com.example.EstateHub_Backend.lead;

import com.example.EstateHub_Backend.property.Property;
import com.example.EstateHub_Backend.property.PropertyRepository;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    // Create Lead
    public Lead createLead(
            Long buyerId,
            Long propertyId,
            String budget,
            java.time.LocalDate preferredVisitDate,
            String message
    ) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() ->
                        new RuntimeException("Buyer not found")
                );

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found")
                );

        if (property.getStatus() !=
                com.example.EstateHub_Backend.property.PropertyStatus.PUBLISHED) {

            throw new RuntimeException(
                    "Lead can only be created for published property"
            );
        }

        Lead lead = Lead.builder()
                .buyer(buyer)
                .property(property)
                .budget(budget)
                .preferredVisitDate(preferredVisitDate)
                .message(message)
                .status(LeadStatus.NEW)
                .build();

        return leadRepository.save(lead);
    }

    // Admin - Get all leads
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    // Get lead by ID
    public Lead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found")
                );
    }
}
package com.example.EstateHub_Backend.lead.dto;

import com.example.EstateHub_Backend.lead.LeadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class LeadResponse {

    private Long id;

    private Long propertyId;

    private String propertyTitle;

    private String budget;

    private LocalDate preferredVisitDate;

    private String message;

    private LeadStatus status;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
package com.example.EstateHub_Backend.lead.dto;

import com.example.EstateHub_Backend.lead.LeadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeadStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private LeadStatus status;

    private String remarks;
}
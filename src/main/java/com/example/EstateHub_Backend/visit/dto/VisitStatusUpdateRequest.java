package com.example.EstateHub_Backend.visit.dto;

import com.example.EstateHub_Backend.visit.VisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private VisitStatus status;

    private String remarks;
}
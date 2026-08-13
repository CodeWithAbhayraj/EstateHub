package com.example.EstateHub_Backend.property.dto;

import com.example.EstateHub_Backend.property.PropertyStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PropertyResponse {

    private Long id;

    private String title;

    private BigDecimal price;

    private Double area;

    private Integer bhk;

    private String propertyType;

    private String city;

    private String locality;

    private String furnished;

    private Boolean parking;

    private String facing;

    private Boolean readyToMove;

    private Boolean newProject;

    private Boolean resale;

    private String description;

    private PropertyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
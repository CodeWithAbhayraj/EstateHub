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

    // =====================================================
    // CITY
    // =====================================================

    private Long cityId;

    private String cityName;

    // =====================================================
    // AREA
    // =====================================================

    private Long areaId;

    private String areaName;

    // =====================================================
    // PROPERTY TYPE
    // =====================================================

    private Long propertyTypeId;

    private String propertyTypeName;

    // =====================================================
    // OTHER PROPERTY DETAILS
    // =====================================================

    private String furnished;

    private Boolean parking;

    private String facing;

    private Boolean readyToMove;

    private Boolean newProject;

    private Boolean resale;

    private String description;

    // =====================================================
    // STATUS
    // =====================================================

    private PropertyStatus status;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
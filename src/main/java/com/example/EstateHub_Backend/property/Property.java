package com.example.EstateHub_Backend.property;

import com.example.EstateHub_Backend.location.Area;
import com.example.EstateHub_Backend.location.City;
import com.example.EstateHub_Backend.location.PropertyType;
import com.example.EstateHub_Backend.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.EstateHub_Backend.property.image.PropertyImage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {


    @OneToMany(
            mappedBy = "property",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PropertyImage> images = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // SELLER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;


    // =====================================================
    // BASIC PROPERTY DETAILS
    // =====================================================

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    // Property ka actual size
    @Column(nullable = false)
    private Double area;

    @Column(nullable = false)
    private Integer bhk;


    // =====================================================
    // LOCATION RELATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "area_id", nullable = false)
    private Area locationArea;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyType propertyType;


    // =====================================================
    // OTHER PROPERTY DETAILS
    // =====================================================

    private String furnished;

    private Boolean parking;

    private String facing;

    private Boolean readyToMove;

    private Boolean newProject;

    private Boolean resale;

    @Column(length = 2000)
    private String description;


    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status;

    @Column(length = 1000)
    private String rejectionReason;


    // =====================================================
    // TIMESTAMPS
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = PropertyStatus.DRAFT;
        }
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}
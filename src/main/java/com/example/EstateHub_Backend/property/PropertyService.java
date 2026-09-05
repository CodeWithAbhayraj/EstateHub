package com.example.EstateHub_Backend.property;

import com.example.EstateHub_Backend.location.Area;
import com.example.EstateHub_Backend.location.AreaRepository;
import com.example.EstateHub_Backend.location.City;
import com.example.EstateHub_Backend.location.CityRepository;
import com.example.EstateHub_Backend.location.PropertyType;
import com.example.EstateHub_Backend.location.PropertyTypeRepository;
import com.example.EstateHub_Backend.notification.NotificationService;
import com.example.EstateHub_Backend.notification.NotificationType;
import com.example.EstateHub_Backend.property.dto.PropertyRequest;
import com.example.EstateHub_Backend.property.dto.PropertyResponse;
import com.example.EstateHub_Backend.property.image.PropertyImage;
import com.example.EstateHub_Backend.user.Role;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    private final CityRepository cityRepository;
    private final AreaRepository areaRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    private final NotificationService notificationService;


    // ==========================================
    // CREATE PROPERTY
    // ==========================================

    @Transactional
    public PropertyResponse createProperty(
            PropertyRequest request,
            String userEmail
    ) {

        User seller = getUserByEmail(userEmail);

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: "
                                        + request.getCityId()
                        )
                );

        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Area not found with id: "
                                        + request.getAreaId()
                        )
                );

        PropertyType propertyType =
                propertyTypeRepository.findById(
                        request.getPropertyTypeId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Property type not found with id: "
                                        + request.getPropertyTypeId()
                        )
                );

        // Area must belong to selected city
        if (!area.getCity().getId().equals(city.getId())) {

            throw new RuntimeException(
                    "Selected area does not belong to selected city"
            );
        }

        Property property = Property.builder()
                .seller(seller)
                .title(request.getTitle())
                .price(request.getPrice())
                .area(request.getArea())
                .bhk(request.getBhk())
                .city(city)
                .locationArea(area)
                .propertyType(propertyType)
                .furnished(request.getFurnished())
                .parking(request.getParking())
                .facing(request.getFacing())
                .readyToMove(request.getReadyToMove())
                .newProject(request.getNewProject())
                .resale(request.getResale())
                .description(request.getDescription())
                .status(PropertyStatus.DRAFT)
                .build();

        Property savedProperty =
                propertyRepository.save(property);

        return mapToResponse(savedProperty);
    }


    // ==========================================
    // GET PROPERTY BY ID
    // ==========================================

    public PropertyResponse getPropertyById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property not found"
                        )
                );

        return mapToResponse(property);
    }


    // ==========================================
    // GET ALL PUBLISHED PROPERTIES
    // ==========================================

    public List<PropertyResponse> getPublishedProperties() {

        return propertyRepository
                .findByStatus(PropertyStatus.PUBLISHED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ==========================================
    // GET SELLER'S PROPERTIES
    // ==========================================

    public List<PropertyResponse> getMyProperties(
            String userEmail
    ) {

        User seller = getUserByEmail(userEmail);

        return propertyRepository
                .findBySellerId(seller.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ==========================================
    // ADMIN - GET ALL PROPERTIES
    // ==========================================

    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {

        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ==========================================
    // UPDATE PROPERTY
    // ==========================================

    @Transactional
    public PropertyResponse updateProperty(
            Long id,
            PropertyRequest request,
            String userEmail
    ) {

        Property property = getProperty(id);

        User seller = getUserByEmail(userEmail);

        if (!property.getSeller().getId().equals(seller.getId())) {

            throw new AccessDeniedException(
                    "You can update only your own property"
            );
        }

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: "
                                        + request.getCityId()
                        )
                );

        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Area not found with id: "
                                        + request.getAreaId()
                        )
                );

        PropertyType propertyType =
                propertyTypeRepository.findById(
                        request.getPropertyTypeId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Property type not found with id: "
                                        + request.getPropertyTypeId()
                        )
                );

        if (!area.getCity().getId().equals(city.getId())) {

            throw new RuntimeException(
                    "Selected area does not belong to selected city"
            );
        }

        property.setTitle(request.getTitle());
        property.setPrice(request.getPrice());
        property.setArea(request.getArea());
        property.setBhk(request.getBhk());

        property.setCity(city);
        property.setLocationArea(area);
        property.setPropertyType(propertyType);

        property.setFurnished(request.getFurnished());
        property.setParking(request.getParking());
        property.setFacing(request.getFacing());
        property.setReadyToMove(request.getReadyToMove());
        property.setNewProject(request.getNewProject());
        property.setResale(request.getResale());
        property.setDescription(request.getDescription());

        Property updatedProperty =
                propertyRepository.save(property);

        return mapToResponse(updatedProperty);
    }


    // ==========================================
    // DELETE PROPERTY
    // ==========================================

    public void deleteProperty(
            Long id,
            String userEmail
    ) {

        Property property = getProperty(id);

        User seller = getUserByEmail(userEmail);

        if (!property.getSeller().getId().equals(seller.getId())) {

            throw new AccessDeniedException(
                    "You can delete only your own property"
            );
        }

        propertyRepository.delete(property);
    }


    // ==========================================
    // SUBMIT FOR APPROVAL
    // ==========================================

    @Transactional
    public PropertyResponse submitForApproval(
            Long id,
            String userEmail
    ) {

        Property property = getProperty(id);

        User seller = getUserByEmail(userEmail);

        if (!property.getSeller().getId().equals(seller.getId())) {

            throw new AccessDeniedException(
                    "You can submit only your own property"
            );
        }

        if (property.getStatus() != PropertyStatus.DRAFT) {

            throw new IllegalStateException(
                    "Only draft properties can be submitted"
            );
        }

        property.setStatus(PropertyStatus.PENDING_APPROVAL);

        Property savedProperty =
                propertyRepository.save(property);


        // ==========================================
        // NOTIFY ADMIN + SUPER ADMIN
        // ==========================================

        List<User> admins = userRepository.findByRoleIn(
                List.of(
                        Role.ADMIN,
                        Role.SUPER_ADMIN
                )
        );

        String message =
                "New property submitted for approval: "
                        + property.getTitle();

        for (User admin : admins) {

            notificationService.createNotification(
                    admin,
                    NotificationType.PROPERTY_SUBMITTED,
                    message,
                    property.getId()
            );
        }

        return mapToResponse(savedProperty);
    }


    // ==========================================
    // ADMIN APPROVE
    // ==========================================

    @Transactional
    public PropertyResponse approveProperty(Long id) {

        Property property = getProperty(id);

        if (property.getStatus() != PropertyStatus.PENDING_APPROVAL) {

            throw new IllegalStateException(
                    "Only pending properties can be approved"
            );
        }

        property.setStatus(PropertyStatus.PUBLISHED);

        // Clear previous rejection reason
        property.setRejectionReason(null);

        Property savedProperty =
                propertyRepository.save(property);


        // ==========================================
        // NOTIFY SELLER
        // ==========================================

        notificationService.createNotification(
                property.getSeller(),
                NotificationType.PROPERTY_APPROVED,
                "Your property has been approved: "
                        + property.getTitle(),
                property.getId()
        );

        return mapToResponse(savedProperty);
    }


    // ==========================================
    // ADMIN REJECT
    // ==========================================

    @Transactional
    public PropertyResponse rejectProperty(
            Long id,
            String reason
    ) {

        if (reason == null || reason.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Rejection reason is required"
            );
        }

        Property property = getProperty(id);

        if (property.getStatus() != PropertyStatus.PENDING_APPROVAL) {

            throw new IllegalStateException(
                    "Only pending properties can be rejected"
            );
        }

        property.setStatus(PropertyStatus.REJECTED);
        property.setRejectionReason(reason.trim());

        Property savedProperty =
                propertyRepository.save(property);


        // ==========================================
        // NOTIFY SELLER
        // ==========================================

        String message =
                "Your property has been rejected: "
                        + property.getTitle()
                        + ". Reason: "
                        + reason.trim();

        notificationService.createNotification(
                property.getSeller(),
                NotificationType.PROPERTY_REJECTED,
                message,
                property.getId()
        );

        return mapToResponse(savedProperty);
    }


    // ==========================================
    // PRIVATE METHODS
    // ==========================================

    private Property getProperty(Long id) {

        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property not found"
                        )
                );
    }


    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }


    // ==========================================
    // ENTITY → RESPONSE
    // ==========================================

    private PropertyResponse mapToResponse(
            Property property
    ) {

        return PropertyResponse.builder()

                .id(property.getId())
                .title(property.getTitle())
                .price(property.getPrice())
                .area(property.getArea())
                .bhk(property.getBhk())

                // PROPERTY TYPE
                .propertyTypeId(
                        property.getPropertyType().getId()
                )
                .propertyType(
                        property.getPropertyType().getName()
                )

                // CITY
                .cityId(
                        property.getCity().getId()
                )
                .city(
                        property.getCity().getName()
                )

                // AREA
                .areaId(
                        property.getLocationArea().getId()
                )
                .areaName(
                        property.getLocationArea().getName()
                )

                // OTHER DETAILS
                .furnished(property.getFurnished())
                .parking(property.getParking())
                .facing(property.getFacing())
                .readyToMove(property.getReadyToMove())
                .newProject(property.getNewProject())
                .resale(property.getResale())
                .description(property.getDescription())

                // STATUS
                .status(property.getStatus())

                // REJECTION REASON
                .rejectionReason(
                        property.getRejectionReason()
                )

                // TIMESTAMPS
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())

                // IMAGES
                .images(
                        property.getImages()
                                .stream()
                                .map(PropertyImage::getImageUrl)
                                .toList()
                )

                .build();
    }
}
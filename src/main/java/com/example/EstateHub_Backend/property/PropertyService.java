package com.example.EstateHub_Backend.property;

import com.example.EstateHub_Backend.property.dto.PropertyRequest;
import com.example.EstateHub_Backend.property.dto.PropertyResponse;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    // ==========================================
    // CREATE PROPERTY
    // ==========================================

    public PropertyResponse createProperty(
            PropertyRequest request,
            String userEmail
    ) {

        User seller = getUserByEmail(userEmail);

        Property property = Property.builder()
                .seller(seller)
                .title(request.getTitle())
                .price(request.getPrice())
                .area(request.getArea())
                .bhk(request.getBhk())
                .propertyType(request.getPropertyType())
                .city(request.getCity())
                .locality(request.getLocality())
                .furnished(request.getFurnished())
                .parking(request.getParking())
                .facing(request.getFacing())
                .readyToMove(request.getReadyToMove())
                .newProject(request.getNewProject())
                .resale(request.getResale())
                .description(request.getDescription())
                .status(PropertyStatus.DRAFT)
                .build();

        Property savedProperty = propertyRepository.save(property);

        return mapToResponse(savedProperty);
    }

    // ==========================================
    // GET PROPERTY BY ID
    // ==========================================

    public PropertyResponse getPropertyById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Property not found")
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
    // UPDATE PROPERTY
    // ==========================================

    public PropertyResponse updateProperty(
            Long id,
            PropertyRequest request,
            String userEmail
    ) {

        Property property = getProperty(id);

        User seller = getUserByEmail(userEmail);

        // Only property owner can update
        if (!property.getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException(
                    "You can update only your own property"
            );
        }

        property.setTitle(request.getTitle());
        property.setPrice(request.getPrice());
        property.setArea(request.getArea());
        property.setBhk(request.getBhk());
        property.setPropertyType(request.getPropertyType());
        property.setCity(request.getCity());
        property.setLocality(request.getLocality());
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

        // Only owner can delete
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

        return mapToResponse(
                propertyRepository.save(property)
        );
    }

    // ==========================================
    // ADMIN APPROVE
    // ==========================================

    public PropertyResponse approveProperty(Long id) {

        Property property = getProperty(id);

        if (property.getStatus() != PropertyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException(
                    "Only pending properties can be approved"
            );
        }

        property.setStatus(PropertyStatus.PUBLISHED);
        property.setRejectionReason(null);

        return mapToResponse(
                propertyRepository.save(property)
        );
    }

    // ==========================================
    // ADMIN REJECT
    // ==========================================

    public PropertyResponse rejectProperty(
            Long id,
            String reason
    ) {

        Property property = getProperty(id);

        if (property.getStatus() != PropertyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException(
                    "Only pending properties can be rejected"
            );
        }

        property.setStatus(PropertyStatus.REJECTED);
        property.setRejectionReason(reason);

        return mapToResponse(
                propertyRepository.save(property)
        );
    }

    // ==========================================
    // PRIVATE METHODS
    // ==========================================

    private Property getProperty(Long id) {

        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Property not found")
                );
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    private PropertyResponse mapToResponse(
            Property property
    ) {

        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .price(property.getPrice())
                .area(property.getArea())
                .bhk(property.getBhk())
                .propertyType(property.getPropertyType())
                .city(property.getCity())
                .locality(property.getLocality())
                .furnished(property.getFurnished())
                .parking(property.getParking())
                .facing(property.getFacing())
                .readyToMove(property.getReadyToMove())
                .newProject(property.getNewProject())
                .resale(property.getResale())
                .description(property.getDescription())
                .status(property.getStatus())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}
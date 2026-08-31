package com.example.EstateHub_Backend.property.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.EstateHub_Backend.exception.BadRequestException;
import com.example.EstateHub_Backend.exception.ResourceNotFoundException;
import com.example.EstateHub_Backend.property.Property;
import com.example.EstateHub_Backend.property.PropertyRepository;
import com.example.EstateHub_Backend.user.User;
import com.example.EstateHub_Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PropertyImageService {

    private final Cloudinary cloudinary;

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final UserRepository userRepository;


    // ==========================================
    // UPLOAD IMAGE
    // ==========================================

    public PropertyImage uploadImage(
            Long propertyId,
            MultipartFile file,
            String userEmail
    ) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found with id: " + propertyId
                        )
                );

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // ==========================================
        // OWNERSHIP CHECK
        // ==========================================

        if (!property.getSeller().getId().equals(user.getId())) {

            throw new AccessDeniedException(
                    "You can upload images only to your own property"
            );
        }


        // ==========================================
        // FILE VALIDATION
        // ==========================================

        if (file == null || file.isEmpty()) {

            throw new BadRequestException(
                    "Image file is required"
            );
        }

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new BadRequestException(
                    "Only image files are allowed"
            );
        }


        // 5 MB limit

        if (file.getSize() > 5 * 1024 * 1024) {

            throw new BadRequestException(
                    "Image size must not exceed 5 MB"
            );
        }


        // ==========================================
        // CLOUDINARY UPLOAD
        // ==========================================

        try {

            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder",
                                    "estatehub/properties/" + propertyId,

                                    "resource_type",
                                    "image"
                            )
                    );

            String imageUrl =
                    uploadResult.get("secure_url").toString();

            String publicId =
                    uploadResult.get("public_id").toString();


            // ==========================================
            // SAVE DATABASE
            // ==========================================

            PropertyImage propertyImage =
                    PropertyImage.builder()
                            .property(property)
                            .imageUrl(imageUrl)
                            .publicId(publicId)
                            .build();

            return propertyImageRepository.save(propertyImage);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload image to Cloudinary"
            );
        }
    }


    // ==========================================
    // GET PROPERTY IMAGES
    // ==========================================

    public List<PropertyImage> getPropertyImages(
            Long propertyId
    ) {

        if (!propertyRepository.existsById(propertyId)) {

            throw new ResourceNotFoundException(
                    "Property not found with id: " + propertyId
            );
        }

        return propertyImageRepository
                .findByPropertyId(propertyId);
    }


    // ==========================================
    // DELETE IMAGE
    // ==========================================

    public void deleteImage(
            Long propertyId,
            Long imageId,
            String userEmail
    ) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found with id: " + propertyId
                        )
                );

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        // ==========================================
        // OWNERSHIP
        // ==========================================

        if (!property.getSeller().getId().equals(user.getId())) {

            throw new AccessDeniedException(
                    "You can delete images only from your own property"
            );
        }


        PropertyImage image =
                propertyImageRepository
                        .findByIdAndPropertyId(
                                imageId,
                                propertyId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Property image not found"
                                )
                        );


        // ==========================================
        // DELETE FROM CLOUDINARY
        // ==========================================

        try {

            cloudinary.uploader().destroy(
                    image.getPublicId(),
                    ObjectUtils.emptyMap()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete image from Cloudinary"
            );
        }


        // ==========================================
        // DELETE FROM DATABASE
        // ==========================================

        propertyImageRepository.delete(image);
    }
}
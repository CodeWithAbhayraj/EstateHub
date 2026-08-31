package com.example.EstateHub_Backend.property.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyImageController {

    private final PropertyImageService propertyImageService;


    // ==========================================
    // UPLOAD IMAGE
    // ==========================================

    @PostMapping(
            value = "/{propertyId}/images",
            consumes = "multipart/form-data"
    )
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyImage> uploadImage(

            @PathVariable Long propertyId,

            @RequestParam("file")
            MultipartFile file,

            Authentication authentication
    ) {

        String email = authentication.getName();

        PropertyImage image =
                propertyImageService.uploadImage(
                        propertyId,
                        file,
                        email
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(image);
    }


    // ==========================================
    // GET IMAGES
    // ==========================================

    @GetMapping("/{propertyId}/images")
    @PreAuthorize(
            "hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN')"
    )
    public ResponseEntity<List<PropertyImage>> getImages(

            @PathVariable Long propertyId
    ) {

        return ResponseEntity.ok(
                propertyImageService.getPropertyImages(
                        propertyId
                )
        );
    }


    // ==========================================
    // DELETE IMAGE
    // ==========================================

    @DeleteMapping("/{propertyId}/images/{imageId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteImage(

            @PathVariable Long propertyId,

            @PathVariable Long imageId,

            Authentication authentication
    ) {

        String email = authentication.getName();

        propertyImageService.deleteImage(
                propertyId,
                imageId,
                email
        );

        return ResponseEntity.noContent().build();
    }
}
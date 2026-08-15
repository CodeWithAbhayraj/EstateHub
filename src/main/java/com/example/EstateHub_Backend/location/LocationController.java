package com.example.EstateHub_Backend.location;

import com.example.EstateHub_Backend.location.dto.AreaRequest;
import com.example.EstateHub_Backend.location.dto.AreaResponse;
import com.example.EstateHub_Backend.location.dto.CityRequest;
import com.example.EstateHub_Backend.location.dto.CityResponse;
import com.example.EstateHub_Backend.location.dto.PropertyTypeRequest;
import com.example.EstateHub_Backend.location.dto.PropertyTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;


    // =====================================================
    // CITY - GET
    // =====================================================

    // Get all active cities
    @GetMapping("/cities")
    public List<CityResponse> getAllCities() {

        return locationService.getAllCities();
    }


    // Get city by ID
    @GetMapping("/cities/{cityId}")
    public CityResponse getCityById(
            @PathVariable Long cityId
    ) {

        return locationService.getCityById(cityId);
    }


    // Search city by exact name
    @GetMapping("/cities/search")
    public CityResponse searchCity(
            @RequestParam String name
    ) {

        return locationService.searchCity(name);
    }


    // =====================================================
    // CITY - CREATE
    // =====================================================

    // Admin creates city
    @PostMapping("/cities")
    public CityResponse createCity(
            @Valid @RequestBody CityRequest request
    ) {

        return locationService.createCity(request);
    }


    // =====================================================
    // AREA - GET
    // =====================================================

    // Get areas by city
    @GetMapping("/cities/{cityId}/areas")
    public List<AreaResponse> getAreasByCity(
            @PathVariable Long cityId
    ) {

        return locationService.getAreasByCity(cityId);
    }


    // Search area by name inside city
    @GetMapping("/areas/search")
    public AreaResponse searchArea(
            @RequestParam String name,
            @RequestParam Long cityId
    ) {

        return locationService.searchArea(name, cityId);
    }


    // =====================================================
    // AREA - CREATE
    // =====================================================

    // Admin creates area inside a city
    @PostMapping("/cities/{cityId}/areas")
    public AreaResponse createArea(
            @PathVariable Long cityId,
            @Valid @RequestBody AreaRequest request
    ) {

        return locationService.createArea(
                cityId,
                request
        );
    }


    // =====================================================
    // PROPERTY TYPE - GET
    // =====================================================

    // Get all active property types
    @GetMapping("/property-types")
    public List<PropertyTypeResponse> getAllPropertyTypes() {

        return locationService.getAllPropertyTypes();
    }


    // Get property type by ID
    @GetMapping("/property-types/{id}")
    public PropertyTypeResponse getPropertyTypeById(
            @PathVariable Long id
    ) {

        return locationService.getPropertyTypeById(id);
    }


    // Search property type by exact name
    @GetMapping("/property-types/search")
    public PropertyTypeResponse searchPropertyType(
            @RequestParam String name
    ) {

        return locationService.searchPropertyType(name);
    }


    // =====================================================
    // PROPERTY TYPE - CREATE
    // =====================================================

    // Admin creates property type
    @PostMapping("/property-types")
    public PropertyTypeResponse createPropertyType(
            @Valid @RequestBody PropertyTypeRequest request
    ) {

        return locationService.createPropertyType(request);
    }
}


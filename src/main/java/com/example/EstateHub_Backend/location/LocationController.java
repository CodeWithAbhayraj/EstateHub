package com.example.EstateHub_Backend.location;

import com.example.EstateHub_Backend.location.dto.CityRequest;
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

    @GetMapping("/cities")
    public List<City> getAllCities() {
        return locationService.getAllCities();
    }

    @GetMapping("/cities/{cityId}")
    public City getCityById(
            @PathVariable Long cityId
    ) {
        return locationService.getCityById(cityId);
    }

    @GetMapping("/cities/search")
    public City searchCity(
            @RequestParam String name
    ) {
        return locationService.searchCity(name);
    }

    // =====================================================
    // CITY - CREATE
    // =====================================================

    @PostMapping("/cities")
    public City createCity(
            @RequestBody CityRequest request
    ) {
        return locationService.createCity(request);
    }

    // =====================================================
    // AREA - GET
    // =====================================================

    @GetMapping("/cities/{cityId}/areas")
    public List<Area> getAreasByCity(
            @PathVariable Long cityId
    ) {
        return locationService.getAreasByCity(cityId);
    }

    @GetMapping("/areas/search")
    public Area searchArea(
            @RequestParam String name,
            @RequestParam Long cityId
    ) {
        return locationService.searchArea(name, cityId);
    }

    // =====================================================
    // AREA - CREATE
    // =====================================================

    @PostMapping("/cities/{cityId}/areas")
    public Area createArea(
            @PathVariable Long cityId,
            @RequestBody Area area
    ) {
        return locationService.createArea(cityId, area);
    }

    // =====================================================
    // PROPERTY TYPE - GET
    // =====================================================

    @GetMapping("/property-types")
    public List<PropertyType> getAllPropertyTypes() {
        return locationService.getAllPropertyTypes();
    }

    @GetMapping("/property-types/{id}")
    public PropertyType getPropertyTypeById(
            @PathVariable Long id
    ) {
        return locationService.getPropertyTypeById(id);
    }

    @GetMapping("/property-types/search")
    public PropertyType searchPropertyType(
            @RequestParam String name
    ) {
        return locationService.searchPropertyType(name);
    }

    // =====================================================
    // PROPERTY TYPE - CREATE
    // =====================================================

    @PostMapping("/property-types")
    public PropertyType createPropertyType(
            @RequestBody PropertyType propertyType
    ) {
        return locationService.createPropertyType(propertyType);
    }
}
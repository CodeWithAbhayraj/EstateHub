package com.example.EstateHub_Backend.location;

import com.example.EstateHub_Backend.location.dto.CityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final CityRepository cityRepository;
    private final AreaRepository areaRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    // =====================================================
    // CITY
    // =====================================================

    @Transactional
    public City createCity(CityRequest request) {

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            throw new RuntimeException("City name is required");
        }

        if (cityRepository.existsByNameIgnoreCase(
                request.getName().trim()
        )) {
            throw new RuntimeException("City already exists");
        }

        City city = new City();

        city.setName(request.getName().trim());
        city.setEnabled(true);

        return cityRepository.save(city);
    }

    public List<City> getAllCities() {

        return cityRepository.findByEnabledTrue();
    }

    public City getCityById(Long id) {

        return cityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: " + id
                        )
                );
    }

    public City searchCity(String name) {

        return cityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found: " + name
                        )
                );
    }

    // =====================================================
    // AREA
    // =====================================================

    @Transactional
    public Area createArea(Long cityId, Area area) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: " + cityId
                        )
                );

        if (areaRepository.existsByNameIgnoreCaseAndCityId(
                area.getName(),
                cityId
        )) {
            throw new RuntimeException(
                    "Area already exists in this city"
            );
        }

        area.setCity(city);
        area.setEnabled(true);

        return areaRepository.save(area);
    }

    public List<Area> getAreasByCity(Long cityId) {

        if (!cityRepository.existsById(cityId)) {
            throw new RuntimeException(
                    "City not found with id: " + cityId
            );
        }

        return areaRepository.findByCityIdAndEnabledTrue(cityId);
    }

    public Area searchArea(String name, Long cityId) {

        return areaRepository
                .findByNameIgnoreCaseAndCityId(name, cityId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Area not found: " + name
                        )
                );
    }

    // =====================================================
    // PROPERTY TYPE
    // =====================================================

    @Transactional
    public PropertyType createPropertyType(
            PropertyType propertyType
    ) {

        if (propertyTypeRepository.existsByNameIgnoreCase(
                propertyType.getName()
        )) {
            throw new RuntimeException(
                    "Property type already exists"
            );
        }

        propertyType.setEnabled(true);

        return propertyTypeRepository.save(propertyType);
    }

    public List<PropertyType> getAllPropertyTypes() {

        return propertyTypeRepository.findByEnabledTrue();
    }

    public PropertyType getPropertyTypeById(Long id) {

        return propertyTypeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property type not found with id: " + id
                        )
                );
    }

    public PropertyType searchPropertyType(String name) {

        return propertyTypeRepository
                .findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property type not found: " + name
                        )
                );
    }
}
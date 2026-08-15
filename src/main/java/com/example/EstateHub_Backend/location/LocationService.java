package com.example.EstateHub_Backend.location;

import com.example.EstateHub_Backend.location.dto.AreaRequest;
import com.example.EstateHub_Backend.location.dto.AreaResponse;
import com.example.EstateHub_Backend.location.dto.CityRequest;
import com.example.EstateHub_Backend.location.dto.CityResponse;
import com.example.EstateHub_Backend.location.dto.PropertyTypeRequest;
import com.example.EstateHub_Backend.location.dto.PropertyTypeResponse;
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
    public CityResponse createCity(CityRequest request) {

        if (cityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("City already exists");
        }

        City city = new City();

        city.setName(request.getName());
        city.setEnabled(true);

        City savedCity = cityRepository.save(city);

        return mapCityToResponse(savedCity);
    }


    public List<CityResponse> getAllCities() {

        return cityRepository.findByEnabledTrue()
                .stream()
                .map(this::mapCityToResponse)
                .toList();
    }


    public CityResponse getCityById(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: " + id
                        )
                );

        return mapCityToResponse(city);
    }


    public CityResponse searchCity(String name) {

        City city = cityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found: " + name
                        )
                );

        return mapCityToResponse(city);
    }


    // =====================================================
    // AREA
    // =====================================================

    @Transactional
    public AreaResponse createArea(
            Long cityId,
            AreaRequest request
    ) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "City not found with id: " + cityId
                        )
                );

        if (areaRepository.existsByNameIgnoreCaseAndCityId(
                request.getName(),
                cityId
        )) {
            throw new RuntimeException(
                    "Area already exists in this city"
            );
        }

        Area area = new Area();

        area.setName(request.getName());
        area.setCity(city);
        area.setEnabled(true);

        Area savedArea = areaRepository.save(area);

        return mapAreaToResponse(savedArea);
    }


    public List<AreaResponse> getAreasByCity(Long cityId) {

        if (!cityRepository.existsById(cityId)) {
            throw new RuntimeException(
                    "City not found with id: " + cityId
            );
        }

        return areaRepository
                .findByCityIdAndEnabledTrue(cityId)
                .stream()
                .map(this::mapAreaToResponse)
                .toList();
    }


    public AreaResponse searchArea(
            String name,
            Long cityId
    ) {

        Area area = areaRepository
                .findByNameIgnoreCaseAndCityId(
                        name,
                        cityId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Area not found: " + name
                        )
                );

        return mapAreaToResponse(area);
    }


    // =====================================================
    // PROPERTY TYPE
    // =====================================================

    @Transactional
    public PropertyTypeResponse createPropertyType(
            PropertyTypeRequest request
    ) {

        if (propertyTypeRepository.existsByNameIgnoreCase(
                request.getName()
        )) {
            throw new RuntimeException(
                    "Property type already exists"
            );
        }

        PropertyType propertyType = new PropertyType();

        propertyType.setName(request.getName());
        propertyType.setEnabled(true);

        PropertyType savedPropertyType =
                propertyTypeRepository.save(propertyType);

        return mapPropertyTypeToResponse(savedPropertyType);
    }


    public List<PropertyTypeResponse> getAllPropertyTypes() {

        return propertyTypeRepository.findByEnabledTrue()
                .stream()
                .map(this::mapPropertyTypeToResponse)
                .toList();
    }


    public PropertyTypeResponse getPropertyTypeById(Long id) {

        PropertyType propertyType =
                propertyTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Property type not found with id: " + id
                                )
                        );

        return mapPropertyTypeToResponse(propertyType);
    }


    public PropertyTypeResponse searchPropertyType(
            String name
    ) {

        PropertyType propertyType =
                propertyTypeRepository
                        .findByNameIgnoreCase(name)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Property type not found: " + name
                                )
                        );

        return mapPropertyTypeToResponse(propertyType);
    }


    // =====================================================
    // ENTITY → CITY RESPONSE
    // =====================================================

    private CityResponse mapCityToResponse(City city) {

        CityResponse response = new CityResponse();

        response.setId(city.getId());
        response.setName(city.getName());
        response.setEnabled(city.getEnabled());

        return response;
    }


    // =====================================================
    // ENTITY → AREA RESPONSE
    // =====================================================

    private AreaResponse mapAreaToResponse(Area area) {

        AreaResponse response = new AreaResponse();

        response.setId(area.getId());
        response.setName(area.getName());
        response.setEnabled(area.getEnabled());

        if (area.getCity() != null) {
            response.setCityId(area.getCity().getId());
            response.setCityName(area.getCity().getName());
        }

        return response;
    }


    // =====================================================
    // ENTITY → PROPERTY TYPE RESPONSE
    // =====================================================

    private PropertyTypeResponse mapPropertyTypeToResponse(
            PropertyType propertyType
    ) {

        PropertyTypeResponse response =
                new PropertyTypeResponse();

        response.setId(propertyType.getId());
        response.setName(propertyType.getName());
        response.setEnabled(propertyType.getEnabled());

        return response;
    }
}
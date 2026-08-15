package com.example.EstateHub_Backend.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {

    Optional<PropertyType> findByNameIgnoreCase(String name);

    List<PropertyType> findByEnabledTrue();

    boolean existsByNameIgnoreCase(String name);
}


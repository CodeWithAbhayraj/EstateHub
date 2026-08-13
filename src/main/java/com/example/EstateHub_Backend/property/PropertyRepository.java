package com.example.EstateHub_Backend.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByStatus(PropertyStatus status);

    List<Property> findBySellerId(Long sellerId);

    List<Property> findByCityIgnoreCase(String city);

    List<Property> findByLocalityIgnoreCase(String locality);
}
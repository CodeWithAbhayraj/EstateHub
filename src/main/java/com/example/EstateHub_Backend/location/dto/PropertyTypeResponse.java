package com.example.EstateHub_Backend.location.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyTypeResponse {

    private Long id;

    private String name;

    private Boolean enabled;
}
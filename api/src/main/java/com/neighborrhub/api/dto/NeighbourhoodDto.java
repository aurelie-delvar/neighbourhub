package com.neighborrhub.api.dto;

import lombok.Data;

@Data
public class NeighbourhoodDto {
    private Long id;
    private String name;
    private String city;
    private String zipcode;
}
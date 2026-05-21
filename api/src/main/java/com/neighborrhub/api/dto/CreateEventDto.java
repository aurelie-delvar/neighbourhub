package com.neighborrhub.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEventDto {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotBlank(message = "Veuillez entrer une date de début")
    private LocalDateTime startsAt;

    @NotBlank(message = "Veuillez entrer un endroit")
    private String location;

    private Integer capacityMax;

    private Long neighbourhoodId;
}

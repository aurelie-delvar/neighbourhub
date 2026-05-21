package com.neighborrhub.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventSummaryDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startsAt;
    private String location;
    private Integer capacityMax;
    private LocalDateTime createdAt;
    private NeighbourhoodDto neighbourhood;
    private UserSummaryDto creator;
}

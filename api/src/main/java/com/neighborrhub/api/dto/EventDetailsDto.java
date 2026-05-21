package com.neighborrhub.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.neighborrhub.api.entity.RsvpStatus;

import lombok.Data;

@Data
public class EventDetailsDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startsAt;
    private String location;
    private Integer capacityMax;
    private LocalDateTime createdAt;
    private NeighbourhoodDto neighbourhood;
    private UserSummaryDto creator;
    private List<RsvpParticipantDto> rsvps;

    @Data
    public static class RsvpParticipantDto {
        private Long id;
        private RsvpStatus status;
        private Long userId;
        private String userName;
    }
}

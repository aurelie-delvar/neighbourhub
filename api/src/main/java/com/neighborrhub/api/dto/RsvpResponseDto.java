package com.neighborrhub.api.dto;

import com.neighborrhub.api.entity.RsvpStatus;

import lombok.Data;

@Data
public class RsvpResponseDto {
    private Long id;
    private RsvpStatus status;
    private Long userId;
    private Long eventId;
}

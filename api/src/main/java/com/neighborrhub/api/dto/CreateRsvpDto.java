package com.neighborrhub.api.dto;

import com.neighborrhub.api.entity.RsvpStatus;

import lombok.Data;

@Data
public class CreateRsvpDto {
    private RsvpStatus status;
}

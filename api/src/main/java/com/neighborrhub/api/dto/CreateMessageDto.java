package com.neighborrhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMessageDto {
    @NotBlank
    private String content;
    private Long receiverId;
}
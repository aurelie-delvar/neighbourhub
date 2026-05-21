package com.neighborrhub.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private UserSummaryDto author;
}

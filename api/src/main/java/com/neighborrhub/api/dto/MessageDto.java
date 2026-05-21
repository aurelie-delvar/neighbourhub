package com.neighborrhub.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDto {
    private Long id;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
    private UserSummaryDto sender;
    private UserSummaryDto receiver;
}

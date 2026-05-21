package com.neighborrhub.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighborrhub.api.dto.CreateMessageDto;
import com.neighborrhub.api.dto.MessageDto;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.service.MessageService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/inbox")
    public List<MessageDto> getAllReceived(@AuthenticationPrincipal User currentUser) {
        return messageService.getAllReceived(currentUser.getId());
    }

    @GetMapping("/sent")
    public List<MessageDto> getAllSent(@AuthenticationPrincipal User currentUser) {
        return messageService.getAllSent(currentUser.getId());
    }

    @PostMapping
    public ResponseEntity<MessageDto> send(@Valid @RequestBody CreateMessageDto dto, 
            @AuthenticationPrincipal User currentUser) {        
        return ResponseEntity.status(201).body(messageService.sendMessage(dto, currentUser));
    }
}
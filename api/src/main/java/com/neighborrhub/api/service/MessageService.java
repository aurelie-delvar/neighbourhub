package com.neighborrhub.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.neighborrhub.api.dto.CreateMessageDto;
import com.neighborrhub.api.dto.MessageDto;
import com.neighborrhub.api.dto.UserSummaryDto;
import com.neighborrhub.api.entity.Message;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.exception.BusinessException;
import com.neighborrhub.api.repositories.MessageRepository;
import com.neighborrhub.api.repositories.UserRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDto> getAllReceived(Long userId) {
        return messageRepository.findByReceiverId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<MessageDto> getAllSent(Long userId) {
        return messageRepository.findBySenderId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public MessageDto sendMessage(CreateMessageDto dto, User currentUser) {
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new BusinessException("Destinataire non trouvé", HttpStatus.NOT_FOUND));

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setRead(false);
        message.setSender(currentUser);
        message.setSentAt(LocalDateTime.now());
        message.setReceiver(receiver);

        return toDto(messageRepository.save(message));
    }
    
    protected MessageDto toDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setContent(message.getContent());
        dto.setRead(message.isRead());
        dto.setSentAt(message.getSentAt());

        UserSummaryDto userDto = new UserSummaryDto();
        userDto.setId(message.getSender().getId());
        userDto.setName(message.getSender().getName());

        dto.setSender(userDto);

        return dto;
    }
}
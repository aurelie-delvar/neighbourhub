package com.neighborrhub.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);

    List<Message> findByReceiverId(Long receiverId);
}

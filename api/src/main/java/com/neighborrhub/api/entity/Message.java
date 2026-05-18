package com.neighborrhub.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_message")
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition="TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable=false)
    private User receiver;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private LocalDateTime sentAt;
}

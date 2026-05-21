package com.neighborrhub.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Rsvp;
import com.neighborrhub.api.entity.RsvpStatus;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<Rsvp> findByEventIdAndUserId(Long eventId, Long userId);

    long countByEventIdAndStatus(Long eventId, RsvpStatus status);
}

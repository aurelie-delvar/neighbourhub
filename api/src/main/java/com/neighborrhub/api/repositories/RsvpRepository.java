package com.neighborrhub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Rsvp;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
    
}

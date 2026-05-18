package com.neighborrhub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}

package com.neighborrhub.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNeighbourhoodId(Long neighbourhoodId);
}

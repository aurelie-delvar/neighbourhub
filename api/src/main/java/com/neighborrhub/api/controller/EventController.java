package com.neighborrhub.api.controller;

import com.neighborrhub.api.repositories.RsvpRepository;
import com.neighborrhub.api.service.RsvpService;
import org.springframework.web.bind.annotation.RestController;

import com.neighborrhub.api.dto.CreateEventDto;
import com.neighborrhub.api.dto.CreateRsvpDto;
import com.neighborrhub.api.dto.EventDetailsDto;
import com.neighborrhub.api.dto.EventSummaryDto;
import com.neighborrhub.api.dto.RsvpResponseDto;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.service.EventService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final RsvpService rsvpService;
    private final RsvpRepository rsvpRepository;
    private final EventService eventService;

    public EventController(EventService eventService, RsvpRepository rsvpRepository, RsvpService rsvpService) {
        this.eventService = eventService;
        this.rsvpRepository = rsvpRepository;
        this.rsvpService = rsvpService;
    }

    @GetMapping
    public List<EventSummaryDto> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDto> getById(@PathVariable Long id) {
        return eventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<EventSummaryDto> create(@Valid @RequestBody CreateEventDto eventDto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(201).body(eventService.create(eventDto, currentUser));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EventSummaryDto> update(@PathVariable Long id, @Valid @RequestBody CreateEventDto updateDto) {
        return eventService.update(id, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!eventService.delete(id))
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

    // ----- ENDPOINTS POUR LES RSVPS -----
    @PostMapping("/{eventId}/rsvp")
    public ResponseEntity<RsvpResponseDto> createRsvp(@PathVariable Long eventId, @AuthenticationPrincipal User currentUser, @Valid @RequestBody CreateRsvpDto createDto) {
        return ResponseEntity.status(201).body(rsvpService.create(eventId, currentUser, createDto));
    }
    
    @PutMapping("/{eventId}/rsvp")
    public ResponseEntity<RsvpResponseDto> updateRsvp(@PathVariable Long eventId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreateRsvpDto updateDto) {
        return rsvpService.update(eventId, currentUser, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{eventId}/rsvp")
    public ResponseEntity<Void> deleteRsvp(@PathVariable Long eventId, @AuthenticationPrincipal User currentUser) {
        if(!rsvpService.delete(eventId, currentUser))
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
    
}
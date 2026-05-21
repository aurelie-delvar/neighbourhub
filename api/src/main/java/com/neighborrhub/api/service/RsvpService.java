package com.neighborrhub.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neighborrhub.api.dto.CreateRsvpDto;
import com.neighborrhub.api.dto.EventSummaryDto;
import com.neighborrhub.api.dto.RsvpResponseDto;
import com.neighborrhub.api.entity.Event;
import com.neighborrhub.api.entity.Rsvp;
import com.neighborrhub.api.entity.RsvpStatus;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.repositories.EventRepository;
import com.neighborrhub.api.repositories.RsvpRepository;

@Service
public class RsvpService {
    private final RsvpRepository rsvpRepository;
    private final EventRepository eventRepository;

    public RsvpService(RsvpRepository rsvpRepository, EventRepository eventRepository) {
        this.rsvpRepository = rsvpRepository;
        this.eventRepository = eventRepository;
    }

    public List<RsvpResponseDto> findAll() {
        return rsvpRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    public Optional<RsvpResponseDto> findById(Long id) {
        return rsvpRepository.findById(id)
                .map(this::toDto);
    }

    public RsvpResponseDto create(Long eventId, User currentUser, CreateRsvpDto createDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evénement introuvable"));

        if (event.getCapacityMax() != null) {
            long currentCount = rsvpRepository.countByEventIdAndStatus(eventId, RsvpStatus.CONFIRMED);
            if (currentCount >= event.getCapacityMax()) {
                throw new RuntimeException("Evénement complet");
            }
        }

        if (rsvpRepository.existsByEventIdAndUserId(eventId, currentUser.getId())) {
            throw new RuntimeException("Déjà inscrit à cet événement");
        }

        Rsvp rsvp = new Rsvp();
        rsvp.setStatus(createDto.getStatus() != null ? createDto.getStatus() : RsvpStatus.CONFIRMED);
        rsvp.setUser(currentUser);
        rsvp.setEvent(event);

        return toDto(rsvpRepository.save(rsvp));
    }

    public Optional<RsvpResponseDto> update(Long eventId, User currentUser, CreateRsvpDto updateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evénement introuvable"));

        return rsvpRepository.findByEventIdAndUserId(eventId, currentUser.getId())
                .map(rsvp -> {
                    if (updateDto.getStatus() == RsvpStatus.CONFIRMED // Le client veut confirmer le rsvp
                            && rsvp.getStatus() != RsvpStatus.CONFIRMED // Check s'il n'est pas déjà inscrit
                            && event.getCapacityMax() != null) { // est-ce que la capacité max a une limite ?
                        long confirmedCount = rsvpRepository.countByEventIdAndStatus(eventId, RsvpStatus.CONFIRMED);

                        if (confirmedCount >= event.getCapacityMax()) {
                            throw new RuntimeException("Evénement complet");
                        }

                        if (updateDto.getStatus() != null) {
                            rsvp.setStatus(updateDto.getStatus());
                        }
                    }
                    return toDto(rsvpRepository.save(rsvp));
                });
    }
    
    public boolean delete(Long eventId, User currentUser) {
        return rsvpRepository.findByEventIdAndUserId(eventId, currentUser.getId())
                .map(rsvp -> {
                    rsvpRepository.delete(rsvp);
                    return true;
                }).orElse(false);
    }

    protected RsvpResponseDto toDto(Rsvp rsvp) {
        RsvpResponseDto dto = new RsvpResponseDto();
        dto.setId(rsvp.getId());
        dto.setStatus(rsvp.getStatus());
        dto.setUserId(rsvp.getUser().getId());

        EventSummaryDto eventDto = new EventSummaryDto();
        eventDto.setId(rsvp.getEvent().getId());

        dto.setEventId(eventDto.getId());

        return dto;
    }
}

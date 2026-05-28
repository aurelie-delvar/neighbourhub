package com.neighborrhub.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.neighborrhub.api.dto.CreateEventDto;
import com.neighborrhub.api.dto.EventDetailsDto;
import com.neighborrhub.api.dto.EventSummaryDto;
import com.neighborrhub.api.dto.NeighbourhoodDto;
import com.neighborrhub.api.dto.UserSummaryDto;
import com.neighborrhub.api.entity.Event;
import com.neighborrhub.api.entity.Neighbourhood;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.exception.BusinessException;
import com.neighborrhub.api.repositories.EventRepository;
import com.neighborrhub.api.repositories.NeighbourhoodRepository;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final NeighbourhoodRepository neighbourhoodRepository;

    public EventService(EventRepository eventRepository, NeighbourhoodRepository neighbourhoodRepository) {
        this.eventRepository = eventRepository;
        this.neighbourhoodRepository = neighbourhoodRepository;
    }

    public List<EventSummaryDto> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    public List<EventSummaryDto> findAllByNeighbourhoodId(Long id) {
        return eventRepository.findByNeighbourhoodId(id)
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    public Optional<EventDetailsDto> findById(Long id) {
        return eventRepository.findById(id)
                .map(this::toDetailsDto);
    }

    public EventSummaryDto create(CreateEventDto createDto, User currentUser) {
        Event event = new Event();
        event.setTitle(createDto.getTitle());
        event.setDescription(createDto.getDescription());
        event.setStartsAt(createDto.getStartsAt());
        event.setLocation(createDto.getLocation());
        event.setCapacityMax(createDto.getCapacityMax());
        event.setNeighbourhood(currentUser.getNeighbourhood());
        event.setUser(currentUser);

        return toSummaryDto(eventRepository.save(event));
    }

    public Optional<EventSummaryDto> update(Long id, CreateEventDto updateDto) {
        Neighbourhood neighbourhood = neighbourhoodRepository.findById(updateDto.getNeighbourhoodId())
                .orElseThrow(() -> new BusinessException("Quartier introuvable", HttpStatus.NOT_FOUND));

        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(updateDto.getTitle());
                    event.setDescription(updateDto.getDescription());
                    event.setStartsAt(updateDto.getStartsAt());
                    event.setLocation(updateDto.getLocation());
                    event.setCapacityMax(updateDto.getCapacityMax());
                    event.setNeighbourhood(neighbourhood);
                    return toSummaryDto(eventRepository.save(event));
                });
    }
    
    public boolean delete(Long id) {
        if (!eventRepository.existsById(id))
            return false;
        eventRepository.deleteById(id);
        return true;
    }

    private EventSummaryDto toSummaryDto(Event event) {
        EventSummaryDto dto = new EventSummaryDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartsAt(event.getStartsAt());
        dto.setLocation(event.getLocation());
        dto.setCapacityMax(event.getCapacityMax());
        dto.setCreatedAt(event.getCreatedAt());

        NeighbourhoodDto neighbourhoodDto = new NeighbourhoodDto();
        neighbourhoodDto.setId(event.getNeighbourhood().getId());
        neighbourhoodDto.setName(event.getNeighbourhood().getName());
        neighbourhoodDto.setCity(event.getNeighbourhood().getCity());
        neighbourhoodDto.setZipcode(event.getNeighbourhood().getZipcode());

        dto.setNeighbourhood(neighbourhoodDto);

        UserSummaryDto creatorDto = new UserSummaryDto();
        creatorDto.setId(event.getUser().getId());
        creatorDto.setName(event.getUser().getName());
        dto.setCreator(creatorDto);

        return dto;
    }
    
    private EventDetailsDto toDetailsDto(Event event) {
        EventDetailsDto dto = new EventDetailsDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartsAt(event.getStartsAt());
        dto.setLocation(event.getLocation());
        dto.setCapacityMax(event.getCapacityMax());
        dto.setCreatedAt(event.getCreatedAt());

        NeighbourhoodDto neighbourhoodDto = new NeighbourhoodDto();
        neighbourhoodDto.setId(event.getNeighbourhood().getId());
        neighbourhoodDto.setName(event.getNeighbourhood().getName());
        neighbourhoodDto.setCity(event.getNeighbourhood().getCity());
        neighbourhoodDto.setZipcode(event.getNeighbourhood().getZipcode());

        dto.setNeighbourhood(neighbourhoodDto);

        UserSummaryDto creatorDto = new UserSummaryDto();
        creatorDto.setId(event.getUser().getId());
        creatorDto.setName(event.getUser().getName());
        dto.setCreator(creatorDto);

        List<EventDetailsDto.RsvpParticipantDto> rsvpDtos = event.getRsvps()
                .stream()
                .map(rsvp -> {
                    EventDetailsDto.RsvpParticipantDto rsvpDto = new EventDetailsDto.RsvpParticipantDto();
                    rsvpDto.setId(rsvp.getId());
                    rsvpDto.setStatus(rsvp.getStatus());
                    rsvpDto.setUserId(rsvp.getUser().getId());
                    rsvpDto.setUserName(rsvp.getUser().getName());
                    return rsvpDto;
                })
                .toList();

        dto.setRsvps(rsvpDtos);
        return dto;
    }
}
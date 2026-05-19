package com.neighborrhub.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neighborrhub.api.dto.NeighbourhoodDto;
import com.neighborrhub.api.entity.Neighbourhood;
import com.neighborrhub.api.repositories.NeighbourhoodRepository;

@Service
public class NeighbourhoodService {
    private final NeighbourhoodRepository neighbourhoodRepository;

    public NeighbourhoodService(NeighbourhoodRepository neighbourhoodRepository) {
        this.neighbourhoodRepository = neighbourhoodRepository;
    }

    public List<NeighbourhoodDto> findAll() {
        return neighbourhoodRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<NeighbourhoodDto> findById(Long id) {
        return neighbourhoodRepository.findById(id)
                .map(this::toDto);
    }

    private NeighbourhoodDto toDto(Neighbourhood neighbourhood) {
        NeighbourhoodDto dto = new NeighbourhoodDto();
        dto.setId(neighbourhood.getId());
        dto.setName(neighbourhood.getName());
        dto.setCity(neighbourhood.getCity());
        dto.setZipcode(neighbourhood.getZipcode());

        return dto;
    }
}
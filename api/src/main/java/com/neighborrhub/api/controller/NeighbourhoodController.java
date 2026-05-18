package com.neighborrhub.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighborrhub.api.dto.NeighbourhoodDto;
import com.neighborrhub.api.repositories.NeighbourhoodRepository;

@RestController
@RequestMapping("/api/neighbourhoods")
public class NeighbourhoodController {
    private final NeighbourhoodRepository neighbourhoodRepository;
    
    public NeighbourhoodController(NeighbourhoodRepository neighbourhoodRepository) {
        this.neighbourhoodRepository = neighbourhoodRepository;
    }

    @GetMapping
    public List<NeighbourhoodDto> getAll() {
        return neighbourhoodRepository.findAll()
            .stream()
            .map(n -> {
                NeighbourhoodDto dto = new NeighbourhoodDto();
                dto.setId(n.getId());
                dto.setName(n.getName());
                dto.setCity(n.getCity());
                dto.setZipcode(n.getZipcode());
                return dto;
            })
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeighbourhoodDto> getById(@PathVariable Long id) {
        return neighbourhoodRepository.findById(id)
                .map(n -> {
                    NeighbourhoodDto dto = new NeighbourhoodDto();
                    dto.setId(n.getId());
                    dto.setName(n.getName());
                    dto.setCity(n.getCity());
                    dto.setZipcode(n.getZipcode());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
}

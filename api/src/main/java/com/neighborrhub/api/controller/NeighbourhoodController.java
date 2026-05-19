package com.neighborrhub.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighborrhub.api.dto.NeighbourhoodDto;
import com.neighborrhub.api.service.NeighbourhoodService;

@RestController
@RequestMapping("/api/neighbourhoods")
public class NeighbourhoodController {
    private final NeighbourhoodService neighbourhoodService;
    
    public NeighbourhoodController(NeighbourhoodService neighbourhoodService) {
        this.neighbourhoodService = neighbourhoodService;
    }

    @GetMapping
    public List<NeighbourhoodDto> getAll() {
        return neighbourhoodService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeighbourhoodDto> getById(@PathVariable Long id) {
        return neighbourhoodService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }   
}
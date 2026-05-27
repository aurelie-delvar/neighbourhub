package com.neighborrhub.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighborrhub.api.dto.AdDto;
import com.neighborrhub.api.dto.CreateAdDto;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.service.AdService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/ads")
public class AdController {
    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping
    public List<AdDto> getAll() {
        return adService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdDto> getById(@PathVariable Long id) {
        return adService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<AdDto> create(@Valid @RequestBody CreateAdDto createDto, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(201).body(adService.create(createDto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdDto> update(@PathVariable Long id, @Valid @RequestBody CreateAdDto updateDto) {
        return adService.update(id, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        adService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

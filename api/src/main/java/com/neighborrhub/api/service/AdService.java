package com.neighborrhub.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.neighborrhub.api.dto.AdDto;
import com.neighborrhub.api.dto.CreateAdDto;
import com.neighborrhub.api.dto.UserSummaryDto;
import com.neighborrhub.api.entity.Ad;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.exception.BusinessException;
import com.neighborrhub.api.repositories.AdRepository;

@Service
public class AdService {
    private final AdRepository adRepository;

    public AdService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public List<AdDto> findAll() {
        return adRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    public Optional<AdDto> findById(Long id) {
        return adRepository.findById(id)
                .map(this::toDto);
    }

    public AdDto create(CreateAdDto createDto, User currentUser) {
        Ad ad = new Ad();
        ad.setTitle(createDto.getTitle());
        ad.setContent(createDto.getContent());
        ad.setUser(currentUser);
        return toDto(adRepository.save(ad));
    }

    public AdDto update(Long id, CreateAdDto updateDto, User currentUser) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Annonce introuvable", HttpStatus.NOT_FOUND));

        if (!ad.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Non autorisé", HttpStatus.FORBIDDEN);
        }

        ad.setTitle(updateDto.getTitle());
        ad.setContent(updateDto.getContent());
        ad.setUpdateDate(LocalDateTime.now());
        return toDto(adRepository.save(ad));
    }
    
    public void delete(Long id, User currentUser) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Annonce introuvable", HttpStatus.NOT_FOUND));

        if (!ad.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Non autorisé", HttpStatus.FORBIDDEN);
        }

        adRepository.deleteById(id);
    }
    

    private AdDto toDto(Ad ad) {
        AdDto dto = new AdDto();
        dto.setId(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setContent(ad.getContent());
        dto.setCreationDate(ad.getCreationDate());
        dto.setUpdateDate(ad.getUpdateDate());

        UserSummaryDto authorDto = new UserSummaryDto();
        authorDto.setId(ad.getUser().getId());
        authorDto.setName(ad.getUser().getName());
        dto.setAuthor(authorDto);

        return dto;
    }
}

package com.neighborrhub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Ad;

public interface AdRepository extends JpaRepository<Ad, Long> {
    
}

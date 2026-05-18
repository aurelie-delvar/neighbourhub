package com.neighborrhub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.Neighbourhood;

public interface NeighbourhoodRepository extends JpaRepository<Neighbourhood, Long> {
    
}

package com.neighborrhub.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighborrhub.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);
}

package com.neighborrhub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neighborrhub.api.dto.UserDto;
import com.neighborrhub.api.entity.User;

@Controller
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(toDto(currentUser));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setMail(user.getMail());
        dto.setName(user.getName());
        dto.setNeighbourhoodId(user.getNeighbourhood().getId());
        return dto;
    }
}

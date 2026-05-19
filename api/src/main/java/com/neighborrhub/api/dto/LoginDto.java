package com.neighborrhub.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank
    @Email
    private String mail;
    
    @NotBlank
    private String password;
}
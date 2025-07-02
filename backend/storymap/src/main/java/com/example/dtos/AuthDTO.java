package com.example.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTO {

    @NotBlank
    @Size(min = 3, max = 30)
    public String username;

    @NotBlank
    @Size(min = 6, max = 30)
    public String password;
}

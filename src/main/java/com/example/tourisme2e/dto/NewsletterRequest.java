package com.example.tourisme2e.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;
}

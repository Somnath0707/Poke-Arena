package com.pokearena.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTrainerRequest(
        @NotBlank(message = " Trainer name cannot be blank")
        @Size(min = 3 , max = 30 , message = " Trainer name cannot be less than 3 or more than 30 Characters")
        String name,
        @NotBlank(message = " Trainer email cannot be blank")
        @Email(message = "Invalid email format")
        String email
) {
}

package com.pokearena.model.dto;

public record TrainerResponseDto(
        Long id,
        String name,
        String email,
        int wins,
        int losses
) {
}

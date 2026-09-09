package com.pokearena.model.dto;

public record AuthResponse(
        String token,
        String trainerName
) {}
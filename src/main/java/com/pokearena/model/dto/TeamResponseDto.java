package com.pokearena.model.dto;

import com.pokearena.entity.Trainer;
import com.pokearena.repository.TrainerRepository;

import java.util.List;

public record TeamResponseDto(
        Long teamId,
        String teamName,
        Long trainerId,
        String trainerName,
        List<TeamMemberResponseDto> members
) {
}

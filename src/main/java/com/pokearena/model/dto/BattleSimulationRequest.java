package com.pokearena.model.dto;

import jakarta.validation.constraints.NotNull;

public record BattleSimulationRequest(
        @NotNull
        Long teamAId,
        @NotNull
        Long teamBId

) {
}

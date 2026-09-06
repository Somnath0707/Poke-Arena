package com.pokearena.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateTeamMemberRequest(

        @NotNull
        Long speciesId,
        @Min(1)
        @Max(100)
        int level,
        @Min(1)
        @Max(6)
        int slotOrder
) {
}

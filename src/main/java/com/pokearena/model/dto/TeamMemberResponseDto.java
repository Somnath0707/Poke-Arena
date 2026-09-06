package com.pokearena.model.dto;

import com.pokearena.model.PokemonType;

public record TeamMemberResponseDto(
        Long id,
        String speciesName,
        PokemonType primaryType,
        int level,
        int slotOrder
) {
}

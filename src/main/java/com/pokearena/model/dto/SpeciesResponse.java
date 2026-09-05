package com.pokearena.model.dto;

import com.pokearena.model.PokemonType;

public record SpeciesResponse(
        long id,
        String name,
        PokemonType primaryType,
        PokemonType secondaryType,
        int baseHp,
        int baseAttack,
        int baseDefense,
        int baseSpeed) {

}

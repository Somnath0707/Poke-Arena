package com.pokearena.engine;

import com.pokearena.model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeEffectivenessMatrixTest {

    private TypeEffectivenessMatrix matrix;

    @BeforeEach
    void setUp() {
        matrix = new TypeEffectivenessMatrix();
    }

    @Test
    @DisplayName("Single-type: Super effective attacks should return 2.0x")
    void testSingleTypeSuperEffective() {
        assertEquals(2.0, matrix.getMultiplier(PokemonType.Water, PokemonType.Fire));
        assertEquals(2.0, matrix.getMultiplier(PokemonType.Electric, PokemonType.Water));
        assertEquals(2.0, matrix.getMultiplier(PokemonType.Fire, PokemonType.Grass));
    }

    @Test
    @DisplayName("Single-type: Resisted attacks should return 0.5x")
    void testSingleTypeResisted() {
        assertEquals(0.5, matrix.getMultiplier(PokemonType.Fire, PokemonType.Water));
        assertEquals(0.5, matrix.getMultiplier(PokemonType.Grass, PokemonType.Fire));
        assertEquals(0.5, matrix.getMultiplier(PokemonType.Electric, PokemonType.Grass));
    }

    @Test
    @DisplayName("Single-type: Immunities should return 0.0x")
    void testSingleTypeImmunity() {
        assertEquals(0.0, matrix.getMultiplier(PokemonType.Electric, PokemonType.Ground));
        assertEquals(0.0, matrix.getMultiplier(PokemonType.Normal, PokemonType.Ghost));
        assertEquals(0.0, matrix.getMultiplier(PokemonType.Ghost, PokemonType.Normal));
    }

    @Test
    @DisplayName("Dual-type: Double super-effective attacks should compound to 4.0x")
    void testDualTypeDoubleSuperEffective() {
        double multiplier = matrix.getDualMultiplier(
                PokemonType.Rock,
                PokemonType.Fire,
                PokemonType.Flying
        );
        assertEquals(4.0, multiplier);
        double swampertMult = matrix.getDualMultiplier(
                PokemonType.Grass,
                PokemonType.Water,
                PokemonType.Ground
        );
        assertEquals(4.0, swampertMult);
    }

    @Test
    @DisplayName("Dual-type: Double resistance should compound to 0.25x")
    void testDualTypeDoubleResistance() {
        double multiplier = matrix.getDualMultiplier(
                PokemonType.Grass,
                PokemonType.Fire,
                PokemonType.Flying
        );
        assertEquals(0.25, multiplier);
    }

    @Test
    @DisplayName("Dual-type: Immunity on one defending type should nullify damage completely (0.0x)")
    void testDualTypeImmunityOverridesWeakness() {
        double multiplier = matrix.getDualMultiplier(
                PokemonType.Electric,
                PokemonType.Water,
                PokemonType.Ground
        );
        assertEquals(0.0, multiplier);
    }

    @Test
    @DisplayName("Dual-type: Weakness and resistance should cancel out to neutral 1.0x")
    void testDualTypeNeutralCancelOut() {
        double multiplier = matrix.getDualMultiplier(
                PokemonType.Ice,
                PokemonType.Grass,
                PokemonType.Fire
        );
        assertEquals(1.0, multiplier);
    }
}
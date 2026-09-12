package com.pokearena.engine;

import com.pokearena.model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleEngineTest {

    private BattleEngine engine;

    @BeforeEach
    void setUp() {
        // Pure Java setup: No Spring Boot or database needed!
        TypeEffectivenessMatrix matrix = new TypeEffectivenessMatrix();
        engine = new BattleEngine(matrix);
    }

    // Helper method to create a clean test Pokemon without boilerplate
    private BattlePokemon makePokemon(String name, PokemonType type, int hp, int attack, int defense, int speed, int level) {
        return new BattlePokemon(name, type, null, hp, attack, defense, speed, level);
    }

    @Test
    @DisplayName("Speed Priority: Faster Pokemon must attack first")
    void testFasterPokemonAttacksFirst() {
        // Fast Jolteon (Speed: 130) vs Slow Snorlax (Speed: 30)
        BattlePokemon fastMon = makePokemon("Jolteon", PokemonType.Electric, 100, 80, 60, 130, 50);
        BattlePokemon slowMon = makePokemon("Snorlax", PokemonType.Normal, 160, 110, 65, 30, 50);

        List<BattlePokemon> teamA = new ArrayList<>(List.of(fastMon));
        List<BattlePokemon> teamB = new ArrayList<>(List.of(slowMon));

        BattleResult result = engine.simulate("Ash", teamA, "Gary", teamB);

        // Check the log to verify Jolteon attacked first
        boolean fastAttackedFirst = false;
        for (String logLine : result.battleLog()) {
            if (logLine.contains("Jolteon") && logLine.contains("attacks first")) {
                fastAttackedFirst = true;
                break;
            }
        }

        assertTrue(fastAttackedFirst, "Jolteon has higher speed and must attack first!");
    }

    @Test
    @DisplayName("Victory Check: Strong team should defeat weak team")
    void testStrongTeamDefeatsWeakTeam() {
        // Overpowered Level 100 Charizard vs Level 5 Magikarp
        BattlePokemon strongMon = makePokemon("Charizard", PokemonType.Fire, 300, 200, 150, 150, 100);
        BattlePokemon weakMon = makePokemon("Magikarp", PokemonType.Water, 20, 10, 10, 10, 5);

        List<BattlePokemon> teamA = new ArrayList<>(List.of(strongMon));
        List<BattlePokemon> teamB = new ArrayList<>(List.of(weakMon));

        BattleResult result = engine.simulate("Ash", teamA, "Gary", teamB);

        assertEquals("Ash", result.winnerName());
        assertEquals("Gary", result.losserName());
        assertTrue(result.roundsPlayed() >= 1);
        assertTrue(weakMon.isFainted());
    }

    @Test
    @DisplayName("Bench Switch-In: When a Pokemon faints, the next teammate is deployed")
    void testBenchSwitchInOnFaint() {
        // Ash has 1 strong Pokemon. Gary has 2 weak Pokemon.
        BattlePokemon strongMon = makePokemon("Mewtwo", PokemonType.Psychic, 250, 180, 100, 130, 80);
        BattlePokemon garyFirst = makePokemon("Pidgey", PokemonType.Normal, 20, 10, 10, 10, 10);
        BattlePokemon garySecond = makePokemon("Rattata", PokemonType.Normal, 25, 12, 10, 15, 10);

        List<BattlePokemon> teamA = new ArrayList<>(List.of(strongMon));
        List<BattlePokemon> teamB = new ArrayList<>(List.of(garyFirst, garySecond));

        BattleResult result = engine.simulate("Ash", teamA, "Gary", teamB);

        // Verify that Gary's second Pokemon was sent out
        boolean secondPokemonSentOut = false;
        for (String line : result.battleLog()) {
            if (line.contains("Gary sends out")) {
                secondPokemonSentOut = true;
                break;
            }
        }

        assertTrue(secondPokemonSentOut, "Gary should have sent out a surviving bench Pokemon after Pidgey fainted!");
        assertEquals("Ash", result.winnerName());
    }

    @Test
    @DisplayName("Move Usage: Battle log must contain real move announcements")
    void testBattleLogContainsMoveNames() {
        BattlePokemon monA = makePokemon("Pikachu", PokemonType.Electric, 100, 70, 50, 90, 50);
        BattlePokemon monB = makePokemon("Squirtle", PokemonType.Water, 100, 50, 70, 40, 50);

        List<BattlePokemon> teamA = new ArrayList<>(List.of(monA));
        List<BattlePokemon> teamB = new ArrayList<>(List.of(monB));

        BattleResult result = engine.simulate("Ash", teamA, "Gary", teamB);

        // Verify attacks are announced with "used <Move>!"
        boolean hasMoveCallout = false;
        for (String line : result.battleLog()) {
            if (line.contains(" used ")) {
                hasMoveCallout = true;
                break;
            }
        }

        assertTrue(hasMoveCallout, "Combat logs should announce move names (e.g. 'Pikachu used Thunderbolt!')");
    }

    @Test
    @DisplayName("Draw Condition: Stalemate match exceeding 100 rounds ends in a Draw")
    void testMatchExceedingMaxTurnsEndsInDraw() {
        // Two indestructible tanks that deal minimal damage and won't faint within 100 turns
        BattlePokemon tankA = makePokemon("Steelix", PokemonType.Steel, 9999, 1, 9999, 50, 50);
        BattlePokemon tankB = makePokemon("Shuckle", PokemonType.Rock, 9999, 1, 9999, 10, 50);

        List<BattlePokemon> teamA = new ArrayList<>(List.of(tankA));
        List<BattlePokemon> teamB = new ArrayList<>(List.of(tankB));

        BattleResult result = engine.simulate("Ash", teamA, "Gary", teamB);

        assertEquals("Draw", result.winnerName());
        assertEquals("Draw", result.losserName());
        assertEquals(100, result.roundsPlayed());
    }
}
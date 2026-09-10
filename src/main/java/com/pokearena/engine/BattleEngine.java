package com.pokearena.engine;

import com.pokearena.model.Move;
import com.pokearena.model.MoveTier;
import com.pokearena.model.PokemonType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class BattleEngine {

    private final TypeEffectivenessMatrix typeEffectivenessMatrix;
    private final Random random = new Random();

    public BattleEngine(TypeEffectivenessMatrix typeEffectivenessMatrix) {
        this.typeEffectivenessMatrix = typeEffectivenessMatrix;
    }

    private BattlePokemon selectNextPokemon(List<BattlePokemon> team, BattlePokemon opponent) {
        List<BattlePokemon> notFainted = new ArrayList<>();
        for (BattlePokemon curr : team) {
            if (!curr.isFainted()) {
                notFainted.add(curr);
            }
        }

        if (notFainted.isEmpty()) return null;

        // 75% chance to try counter-picking, 25% wildcard
        boolean tryCounter = random.nextInt(100) < 75;

        if (tryCounter) {
            for (BattlePokemon bench : notFainted) {
                double currStatOne = typeEffectivenessMatrix.getDualMultiplier(bench.getType(), opponent.getType(), opponent.getSecondaryType());
                double currStateTwo = 0.0;
                if (bench.getSecondaryType() != null)
                    currStateTwo = typeEffectivenessMatrix.getDualMultiplier(bench.getSecondaryType(), opponent.getType(), opponent.getSecondaryType());

                if (currStatOne >= 2.0 || currStateTwo >= 2.0) {
                    return bench;
                }
            }
        }

        // Fallback or 25% Wildcard pick
        int currRandom = random.nextInt(notFainted.size());
        return notFainted.get(currRandom);
    }

    public BattleResult simulate(
            String trainerNameA, List<BattlePokemon> teamA,
            String trainerNameB, List<BattlePokemon> teamB
    ) {
        List<String> battleLog = new ArrayList<>();
        int turn = 1;

        battleLog.add(
                "Battle has started between " + trainerNameA +
                        " and Challenger " + trainerNameB +
                        "! This is going to be a once in a lifetime battle, so buckle up!"
        );

        // Active fighters
        BattlePokemon pokemonA = teamA.get(0);
        BattlePokemon pokemonB = teamB.get(0);

        battleLog.add(trainerNameA + " sends out " + pokemonA.getName() + "!");
        battleLog.add(trainerNameB + " sends out " + pokemonB.getName() + "!");

        while (pokemonA != null && pokemonB != null) {

            battleLog.add("--- Round " + turn + " ---");

            BattlePokemon firstAttacker;
            BattlePokemon secondAttacker;

            // Speed Priority Check
            if (pokemonA.getSpeed() >= pokemonB.getSpeed()) {
                firstAttacker = pokemonA;
                secondAttacker = pokemonB;
            } else {
                firstAttacker = pokemonB;
                secondAttacker = pokemonA;
            }

            battleLog.add(
                    firstAttacker.getName() + " (Speed: " +
                            firstAttacker.getSpeed() + ") attacks first."
            );

            // 1. First Attacker executes move
            executeAttack(firstAttacker, secondAttacker, battleLog);

            if (secondAttacker.isFainted()) {
                battleLog.add(secondAttacker.getName() + " fainted!");

                if (secondAttacker == pokemonA) {
                    pokemonA = selectNextPokemon(teamA, pokemonB);
                    if (pokemonA != null) {
                        battleLog.add(trainerNameA + " sends out " + pokemonA.getName() + "!");
                    }
                } else {
                    pokemonB = selectNextPokemon(teamB, pokemonA);
                    if (pokemonB != null) {
                        battleLog.add(trainerNameB + " sends out " + pokemonB.getName() + "!");
                    }
                }
            } else {
                // 2. Second Attacker counter-attacks if still standing
                battleLog.add(
                        secondAttacker.getName() + " survived with " +
                                secondAttacker.getCurrHp() + "/" + secondAttacker.getMaxHp() +
                                " HP and counter-attacks."
                );

                executeAttack(secondAttacker, firstAttacker, battleLog);

                if (firstAttacker.isFainted()) {
                    battleLog.add(firstAttacker.getName() + " fainted!");

                    if (firstAttacker == pokemonA) {
                        pokemonA = selectNextPokemon(teamA, pokemonB);
                        if (pokemonA != null) {
                            battleLog.add(trainerNameA + " sends out " + pokemonA.getName() + "!");
                        }
                    } else {
                        pokemonB = selectNextPokemon(teamB, pokemonA);
                        if (pokemonB != null) {
                            battleLog.add(trainerNameB + " sends out " + pokemonB.getName() + "!");
                        }
                    }
                }
            }

            turn++;

            if (turn > 100) {
                battleLog.add("Match exceeded 100 turns. Declared a draw.");
                break;
            }
        }

        String winnerName;
        String loserName;

        // Determine outcome
        if (pokemonA != null && pokemonB != null) {
            winnerName = "Draw";
            loserName = "Draw";
        } else if (pokemonA != null) {
            winnerName = trainerNameA;
            loserName = trainerNameB;
        } else {
            winnerName = trainerNameB;
            loserName = trainerNameA;
        }

        if (!winnerName.equals("Draw")) {
            battleLog.add(winnerName + " wins the battle in " + (turn - 1) + " rounds!");
        }

        return new BattleResult(
                winnerName,
                loserName,
                turn - 1,
                battleLog
        );
    }

    /**
     * Executes an attack by rolling a move from the Pokemon's elemental arsenal,
     * checking accuracy, calculating STAB + dual type effectiveness, and dealing damage.
     */
    private void executeAttack(
            BattlePokemon attacker,
            BattlePokemon defender,
            List<String> battleLog
    ) {
        Move move = rollMoveForPokemon(attacker);

        battleLog.add(attacker.getName() + " used " + move.getDisplayName() + "!");

        // 1. Accuracy Check (Hit or Miss)
        if (random.nextInt(100) >= move.getAccuracy()) {
            battleLog.add("...but the attack missed!");
            return;
        }

        // 2. Compound Dual-Type Multiplier (e.g. Rock vs Fire/Flying = 4.0x)
        double multiplier = typeEffectivenessMatrix.getDualMultiplier(
                move.getType(),
                defender.getType(),
                defender.getSecondaryType()
        );

        if (multiplier == 0.0) {
            battleLog.add("It had no effect on " + defender.getName() + "!");
            return;
        } else if (multiplier >= 2.0) {
            battleLog.add("It's super effective! (" + multiplier + "x)");
        } else if (multiplier <= 0.5) {
            battleLog.add("It's not very effective... (" + multiplier + "x)");
        }

        // 3. STAB (Same-Type Attack Bonus): 1.5x damage if using own type
        double stab = (move.getType() == attacker.getType() || move.getType() == attacker.getSecondaryType())
                ? 1.5
                : 1.0;

        // 4. Authentic Pokémon Damage Math
        double baseDamage = (((2.0 * attacker.getLevel() / 5.0) + 2.0)
                * move.getPower()
                * ((double) attacker.getAttack() / Math.max(1, defender.getDefence()))
                / 50.0 + 2.0)
                * stab
                * multiplier;

        int finalDamage = Math.max(1, (int) baseDamage);
        defender.takeDamage(finalDamage);

        battleLog.add(attacker.getName() + " deals " + finalDamage + " damage to " + defender.getName() + "!");
    }

    /**
     * Dynamically pulls moves matching the Pokemon's Primary Type, Secondary Type,
     * and Universal Normal moves.
     * Features "Desperation Mode" (< 25% HP) for clutch Heavy/Ultimate moves!
     */
    private Move rollMoveForPokemon(BattlePokemon pokemon) {
        // Collect all moves in the Pokemon's arsenal
        List<Move> arsenal = Arrays.stream(Move.values())
                .filter(m -> m.getType() == pokemon.getType()
                        || m.getType() == pokemon.getSecondaryType()
                        || m.getType() == PokemonType.Normal)
                .toList();

        // Check if Pokemon is at critical HP (< 25%)
        boolean isCriticalHp = ((double) pokemon.getCurrHp() / pokemon.getMaxHp()) < 0.25;

        int roll = random.nextInt(100);
        MoveTier targetTier;

        if (isCriticalHp) {
            // Clutch Mode: Higher chance for Heavy (25%) & Ultimate (15%)!
            if (roll < 20) targetTier = MoveTier.LIGHT;
            else if (roll < 60) targetTier = MoveTier.STANDARD;
            else if (roll < 85) targetTier = MoveTier.HEAVY;
            else targetTier = MoveTier.ULTIMATE;
        } else {
            // Normal state: 35% Light, 45% Standard, 15% Heavy, 5% Ultimate
            if (roll < 35) targetTier = MoveTier.LIGHT;
            else if (roll < 80) targetTier = MoveTier.STANDARD;
            else if (roll < 95) targetTier = MoveTier.HEAVY;
            else targetTier = MoveTier.ULTIMATE;
        }

        // Filter arsenal by the rolled tier
        final MoveTier tierToFind = targetTier;
        List<Move> matchingMoves = arsenal.stream()
                .filter(m -> m.getTier() == tierToFind)
                .toList();

        // If no move exists in that tier for this type, fallback to any move in the arsenal
        if (matchingMoves.isEmpty()) {
            return arsenal.get(random.nextInt(arsenal.size()));
        }

        return matchingMoves.get(random.nextInt(matchingMoves.size()));
    }
}
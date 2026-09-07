package com.pokearena.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BattleEngine {

    private final TypeEffectivenessMatrix typeEffectivenessMatrix;

    public BattleEngine(TypeEffectivenessMatrix typeEffectivenessMatrix) {
        this.typeEffectivenessMatrix = typeEffectivenessMatrix;
    }

    public BattleResult simulate(
            String trainerNameA, List<BattlePokemon> teamA,
            String trainerNameB, List<BattlePokemon> teamB
    ) {

        List<String> battleLog = new ArrayList<>();
        int aIndex = 0;
        int bIndex = 0;
        int turn = 1;

        battleLog.add(
                "Battle has started between " + trainerNameA +
                        " and Challenger " + trainerNameB +
                        "! This is going to be a once in a lifetime battle, so buckle up!"
        );

        battleLog.add(
                trainerNameA + " sends out " + teamA.get(0).getName() + "!"
        );

        battleLog.add(
                trainerNameB + " sends out " + teamB.get(0).getName() + "!"
        );

        while ( aIndex < teamA.size() && bIndex < teamB.size()) {

            BattlePokemon pokemonA = teamA.get(aIndex);
            BattlePokemon pokemonB = teamB.get(bIndex);

            battleLog.add("--- Round " + turn + " ---");

            BattlePokemon firstAttacker;
            BattlePokemon secondAttacker;

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

            executeAttack(firstAttacker, secondAttacker, battleLog);

            if (secondAttacker.isFainted()) {

                battleLog.add(secondAttacker.getName() + " fainted!");

                if (secondAttacker == pokemonA) {
                    aIndex++;

                    if (aIndex < teamA.size()) {
                        battleLog.add(
                                trainerNameA + " sends out " +
                                        teamA.get(aIndex).getName() + "!"
                        );
                    }

                } else {
                    bIndex++;

                    if (bIndex < teamB.size()) {
                        battleLog.add(
                                trainerNameB + " sends out " +
                                        teamB.get(bIndex).getName() + "!"
                        );
                    }
                }

            } else {

                battleLog.add(
                        secondAttacker.getName() +
                                " survived with " +
                                secondAttacker.getCurrHp() +
                                "/" +
                                secondAttacker.getMaxHp() +
                                " HP and counter-attacks."
                );

                executeAttack(secondAttacker, firstAttacker, battleLog);

                if (firstAttacker.isFainted()) {

                    battleLog.add(firstAttacker.getName() + " fainted!");

                    if (firstAttacker == pokemonA) {
                        aIndex++;

                        if (aIndex < teamA.size()) {
                            battleLog.add(
                                    trainerNameA + " sends out " +
                                            teamA.get(aIndex).getName() + "!"
                            );
                        }

                    } else {
                        bIndex++;

                        if (bIndex < teamB.size()) {
                            battleLog.add(
                                    trainerNameB + " sends out " +
                                            teamB.get(bIndex).getName() + "!"
                            );
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

        if (aIndex < teamA.size()) {
            winnerName = trainerNameA;
            loserName = trainerNameB;
        } else {
            winnerName = trainerNameB;
            loserName = trainerNameA;
        }

        battleLog.add(
                winnerName + " wins the battle in " + (turn - 1) + " rounds!"
        );

        return new BattleResult(
                winnerName,
                loserName,
                turn - 1,
                battleLog
        );
    }

    private void executeAttack(
            BattlePokemon attacker,
            BattlePokemon defender,
            List<String> battleLog
    ) {
        int damage = calculateDamage(attacker, defender, battleLog);

        defender.takeDamage(damage);

        battleLog.add(
                attacker.getName() +
                        " deals " +
                        damage +
                        " damage to " +
                        defender.getName() +
                        "!"
        );
    }

    private int calculateDamage(
            BattlePokemon attacker,
            BattlePokemon defender,
            List<String> battleLog
    ) {
        double multiplier = typeEffectivenessMatrix.getMultiplier(
                attacker.getType(),
                defender.getType()
        );

        if (multiplier == 2.0) {
            battleLog.add("It's super effective!");
        } else if (multiplier == 0.5) {
            battleLog.add("It's not very effective...");
        } else if (multiplier == 0.0) {
            battleLog.add(
                    "It has no effect on " +
                            defender.getName() + "!"
            );
            return 0;
        }

        double baseDamage =
                ((2.0 * attacker.getLevel() / 5.0) + 2.0)
                        * ((double) attacker.getAttack() / defender.getDefence())
                        * multiplier;

        return Math.max(1, (int) baseDamage);
    }
}
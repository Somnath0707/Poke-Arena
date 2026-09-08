package com.pokearena.engine;

import com.pokearena.model.PokemonType;
import org.springframework.stereotype.Component;

@Component
public class TypeEffectivenessMatrix {

    public double getMultiplier(
            PokemonType attackType,
            PokemonType defenseType
    ) {

        switch (attackType) {

            case Normal:
                if (defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                if (defenseType == PokemonType.Ghost) {
                    return 0.0;
                }
                break;

            case Fire:
                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Ice ||
                        defenseType == PokemonType.Bug ||
                        defenseType == PokemonType.Steel) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Dragon) {
                    return 0.5;
                }
                break;

            case Water:
                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Ground ||
                        defenseType == PokemonType.Rock) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Dragon) {
                    return 0.5;
                }
                break;

            case Electric:
                if (defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Flying) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Electric ||
                        defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Dragon) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Ground) {
                    return 0.0;
                }
                break;

            case Grass:
                if (defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Ground ||
                        defenseType == PokemonType.Rock) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Flying ||
                        defenseType == PokemonType.Bug ||
                        defenseType == PokemonType.Dragon ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;

            case Ice:
                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Ground ||
                        defenseType == PokemonType.Flying ||
                        defenseType == PokemonType.Dragon) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Ice ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;

            case Fighting:
                if (defenseType == PokemonType.Normal ||
                        defenseType == PokemonType.Ice ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Dark ||
                        defenseType == PokemonType.Steel) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Flying ||
                        defenseType == PokemonType.Psychic ||
                        defenseType == PokemonType.Bug ||
                        defenseType == PokemonType.Fairy) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Ghost) {
                    return 0.0;
                }
                break;

            case Poison:
                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Fairy) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Ground ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Ghost) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Steel) {
                    return 0.0;
                }
                break;

            case Ground:
                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Electric ||
                        defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Steel) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Bug) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Flying) {
                    return 0.0;
                }
                break;

            case Flying:
                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Bug) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Electric ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;

            case Psychic:
                if (defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Poison) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Psychic ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Dark) {
                    return 0.0;
                }
                break;

            case Bug:
                if (defenseType == PokemonType.Grass ||
                        defenseType == PokemonType.Psychic ||
                        defenseType == PokemonType.Dark) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Flying ||
                        defenseType == PokemonType.Ghost ||
                        defenseType == PokemonType.Steel ||
                        defenseType == PokemonType.Fairy) {
                    return 0.5;
                }
                break;

            case Rock:
                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Ice ||
                        defenseType == PokemonType.Flying ||
                        defenseType == PokemonType.Bug) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Ground ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;

            case Ghost:
                if (defenseType == PokemonType.Psychic ||
                        defenseType == PokemonType.Ghost) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Dark) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Normal) {
                    return 0.0;
                }
                break;

            case Dragon:
                if (defenseType == PokemonType.Dragon) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Steel) {
                    return 0.5;
                }

                if (defenseType == PokemonType.Fairy) {
                    return 0.0;
                }
                break;

            case Dark:
                if (defenseType == PokemonType.Psychic ||
                        defenseType == PokemonType.Ghost) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Dark ||
                        defenseType == PokemonType.Fairy) {
                    return 0.5;
                }
                break;

            case Steel:
                if (defenseType == PokemonType.Ice ||
                        defenseType == PokemonType.Rock ||
                        defenseType == PokemonType.Fairy) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Water ||
                        defenseType == PokemonType.Electric ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;

            case Fairy:
                if (defenseType == PokemonType.Fighting ||
                        defenseType == PokemonType.Dragon ||
                        defenseType == PokemonType.Dark) {
                    return 2.0;
                }

                if (defenseType == PokemonType.Fire ||
                        defenseType == PokemonType.Poison ||
                        defenseType == PokemonType.Steel) {
                    return 0.5;
                }
                break;
        }

        // Any matchup not explicitly mentioned is neutral.
        return 1.0;
    }
}
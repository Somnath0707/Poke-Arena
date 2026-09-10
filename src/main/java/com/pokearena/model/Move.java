package com.pokearena.model;

import java.util.Arrays;
import java.util.List;

public enum Move {
    TACKLE("Tackle", PokemonType.Normal, 40, 100, MoveTier.LIGHT),
    QUICK_ATTACK("Quick Attack", PokemonType.Normal, 40, 100, MoveTier.LIGHT),
    SLASH("Slash", PokemonType.Normal, 70, 100, MoveTier.STANDARD),
    BODY_SLAM("Body Slam", PokemonType.Normal, 85, 100, MoveTier.STANDARD),
    HYPER_BEAM("Hyper Beam", PokemonType.Normal, 150, 70, MoveTier.ULTIMATE),
    // === FIRE ===
    EMBER("Ember", PokemonType.Fire, 40, 100, MoveTier.LIGHT),
    FLAME_WHEEL("Flame Wheel", PokemonType.Fire, 60, 100, MoveTier.LIGHT),
    FLAMETHROWER("Flamethrower", PokemonType.Fire, 90, 100, MoveTier.STANDARD),
    FIRE_BLAST("Fire Blast", PokemonType.Fire, 110, 85, MoveTier.HEAVY),
    OVERHEAT("Overheat", PokemonType.Fire, 130, 80, MoveTier.ULTIMATE),
    // === WATER ===
    WATER_GUN("Water Gun", PokemonType.Water, 40, 100, MoveTier.LIGHT),
    AQUA_JET("Aqua Jet", PokemonType.Water, 40, 100, MoveTier.LIGHT),
    SURF("Surf", PokemonType.Water, 90, 100, MoveTier.STANDARD),
    HYDRO_PUMP("Hydro Pump", PokemonType.Water, 110, 80, MoveTier.HEAVY),
    WATER_SPOUT("Water Spout", PokemonType.Water, 150, 75, MoveTier.ULTIMATE),
    // === GRASS ===
    VINE_WHIP("Vine Whip", PokemonType.Grass, 45, 100, MoveTier.LIGHT),
    RAZOR_LEAF("Razor Leaf", PokemonType.Grass, 55, 95, MoveTier.LIGHT),
    ENERGY_BALL("Energy Ball", PokemonType.Grass, 90, 100, MoveTier.STANDARD),
    LEAF_BLADE("Leaf Blade", PokemonType.Grass, 90, 100, MoveTier.STANDARD),
    SOLAR_BEAM("Solar Beam", PokemonType.Grass, 120, 90, MoveTier.ULTIMATE),
    // === ELECTRIC ===
    THUNDER_SHOCK("Thunder Shock", PokemonType.Electric, 40, 100, MoveTier.LIGHT),
    SPARK("Spark", PokemonType.Electric, 65, 100, MoveTier.LIGHT),
    THUNDERBOLT("Thunderbolt", PokemonType.Electric, 90, 100, MoveTier.STANDARD),
    THUNDER("Thunder", PokemonType.Electric, 110, 70, MoveTier.HEAVY),
    ZAP_CANNON("Zap Cannon", PokemonType.Electric, 140, 60, MoveTier.ULTIMATE),
    // === ICE ===
    ICE_SHARD("Ice Shard", PokemonType.Ice, 40, 100, MoveTier.LIGHT),
    ICY_WIND("Icy Wind", PokemonType.Ice, 55, 95, MoveTier.LIGHT),
    ICE_BEAM("Ice Beam", PokemonType.Ice, 90, 100, MoveTier.STANDARD),
    BLIZZARD("Blizzard", PokemonType.Ice, 110, 70, MoveTier.HEAVY),
    // === FIGHTING ===
    MACH_PUNCH("Mach Punch", PokemonType.Fighting, 40, 100, MoveTier.LIGHT),
    BRICK_BREAK("Brick Break", PokemonType.Fighting, 75, 100, MoveTier.STANDARD),
    CROSS_CHOP("Cross Chop", PokemonType.Fighting, 100, 80, MoveTier.HEAVY),
    CLOSE_COMBAT("Close Combat", PokemonType.Fighting, 120, 100, MoveTier.ULTIMATE),
    // === POISON ===
    POISON_STING("Poison Sting", PokemonType.Poison, 35, 100, MoveTier.LIGHT),
    CROSS_POISON("Cross Poison", PokemonType.Poison, 70, 100, MoveTier.LIGHT),
    SLUDGE_BOMB("Sludge Bomb", PokemonType.Poison, 90, 100, MoveTier.STANDARD),
    GUNK_SHOT("Gunk Shot", PokemonType.Poison, 120, 80, MoveTier.HEAVY),
    // === GROUND ===
    MUD_SLAP("Mud-Slap", PokemonType.Ground, 30, 100, MoveTier.LIGHT),
    BULLDOZE("Bulldoze", PokemonType.Ground, 60, 100, MoveTier.LIGHT),
    EARTHQUAKE("Earthquake", PokemonType.Ground, 100, 100, MoveTier.STANDARD),
    EARTH_POWER("Earth Power", PokemonType.Ground, 90, 100, MoveTier.STANDARD),
    // === FLYING ===
    PECK("Peck", PokemonType.Flying, 35, 100, MoveTier.LIGHT),
    WING_ATTACK("Wing Attack", PokemonType.Flying, 60, 100, MoveTier.LIGHT),
    AIR_SLASH("Air Slash", PokemonType.Flying, 75, 95, MoveTier.STANDARD),
    BRAVE_BIRD("Brave Bird", PokemonType.Flying, 120, 100, MoveTier.ULTIMATE),
    // === PSYCHIC ===
    CONFUSION("Confusion", PokemonType.Psychic, 50, 100, MoveTier.LIGHT),
    PSYBEAM("Psybeam", PokemonType.Psychic, 65, 100, MoveTier.LIGHT),
    PSYCHIC("Psychic", PokemonType.Psychic, 90, 100, MoveTier.STANDARD),
    FUTURE_SIGHT("Future Sight", PokemonType.Psychic, 120, 100, MoveTier.HEAVY),
    // === BUG ===
    BUG_BITE("Bug Bite", PokemonType.Bug, 60, 100, MoveTier.LIGHT),
    X_SCISSOR("X-Scissor", PokemonType.Bug, 80, 100, MoveTier.STANDARD),
    BUG_BUZZ("Bug Buzz", PokemonType.Bug, 90, 100, MoveTier.STANDARD),
    MEGAHORN("Megahorn", PokemonType.Bug, 120, 85, MoveTier.HEAVY),
    // === ROCK ===
    ROCK_THROW("Rock Throw", PokemonType.Rock, 50, 90, MoveTier.LIGHT),
    ROCK_SLIDE("Rock Slide", PokemonType.Rock, 75, 90, MoveTier.STANDARD),
    STONE_EDGE("Stone Edge", PokemonType.Rock, 100, 80, MoveTier.HEAVY),
    // === GHOST ===
    ASTONISH("Astonish", PokemonType.Ghost, 30, 100, MoveTier.LIGHT),
    SHADOW_SNEAK("Shadow Sneak", PokemonType.Ghost, 40, 100, MoveTier.LIGHT),
    SHADOW_BALL("Shadow Ball", PokemonType.Ghost, 80, 100, MoveTier.STANDARD),
    SHADOW_CLAW("Shadow Claw", PokemonType.Ghost, 70, 100, MoveTier.STANDARD),
    // === DRAGON ===
    DRAGON_BREATH("Dragon Breath", PokemonType.Dragon, 60, 100, MoveTier.LIGHT),
    DRAGON_CLAW("Dragon Claw", PokemonType.Dragon, 80, 100, MoveTier.STANDARD),
    DRACO_METEOR("Draco Meteor", PokemonType.Dragon, 130, 85, MoveTier.HEAVY),
    OUTRAGE("Outrage", PokemonType.Dragon, 120, 100, MoveTier.ULTIMATE),
    // === DARK ===
    BITE("Bite", PokemonType.Dark, 60, 100, MoveTier.LIGHT),
    FEINT_ATTACK("Feint Attack", PokemonType.Dark, 60, 100, MoveTier.LIGHT),
    CRUNCH("Crunch", PokemonType.Dark, 80, 100, MoveTier.STANDARD),
    DARK_PULSE("Dark Pulse", PokemonType.Dark, 80, 100, MoveTier.STANDARD),
    // === STEEL ===
    METAL_CLAW("Metal Claw", PokemonType.Steel, 50, 95, MoveTier.LIGHT),
    FLASH_CANNON("Flash Cannon", PokemonType.Steel, 80, 100, MoveTier.STANDARD),
    IRON_HEAD("Iron Head", PokemonType.Steel, 80, 100, MoveTier.STANDARD),
    // === FAIRY ===
    DISARMING_VOICE("Disarming Voice", PokemonType.Fairy, 40, 100, MoveTier.LIGHT),
    DRAINING_KISS("Draining Kiss", PokemonType.Fairy, 50, 100, MoveTier.LIGHT),
    DAZZLING_GLEAM("Dazzling Gleam", PokemonType.Fairy, 80, 100, MoveTier.STANDARD),
    MOONBLAST("Moonblast", PokemonType.Fairy, 95, 100, MoveTier.HEAVY);

    private final String displayName;
    private final PokemonType type;
    private final int power;
    private final int accuracy;
    private final MoveTier tier;

    Move(String displayName , PokemonType type ,int power , int accuracy , MoveTier tier ){
        this.displayName = displayName;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.tier = tier ;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PokemonType getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public MoveTier getTier() {
        return tier;
    }

    public  static List<Move> getMoveTypes(PokemonType type){
        return Arrays.stream(values())
                .filter(m->m.getType() == type)
                .toList();
    }
}

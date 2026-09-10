/**
 * Centralized registry mapping Pokémon moves to specific animation configurations.
 * Used by BattleDirector to orchestrate multi-phase GSAP-powered attack animations.
 */
window.MoveVisualRegistry = (function() {
    
    // Tier definitions for fallback (calibrated for readable broadcast pacing)
    const TIERS = {
        LIGHT: { chargeTime: 260, strikeTime: 220, impactTime: 180, lungeStyle: 'light', shakeIntensity: 'light', screenDim: false },
        STANDARD: { chargeTime: 420, strikeTime: 280, impactTime: 240, lungeStyle: 'standard', shakeIntensity: 'normal', screenDim: false },
        HEAVY: { chargeTime: 550, strikeTime: 340, impactTime: 320, lungeStyle: 'heavy', shakeIntensity: 'heavy', screenDim: true },
        ULTIMATE: { chargeTime: 750, strikeTime: 400, impactTime: 400, lungeStyle: 'ultimate', shakeIntensity: 'heavy', screenDim: true }
    };

    const moves = {
        // Normal
        "TACKLE": { vfx: 'castImpact', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'normal', chargeTime: 100, strikeTime: 150, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "QUICK ATTACK": { vfx: 'castImpact', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'normal', chargeTime: 80, strikeTime: 100, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "SLASH": { vfx: 'castSlash', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'normal', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "BODY SLAM": { vfx: 'castSlam', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'normal', chargeTime: 220, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },
        "HYPER BEAM": { vfx: 'castHyperBeam', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'normal', chargeTime: 450, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },
        
        // Fire
        "EMBER": { vfx: 'castFire', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'fire', chargeTime: 100, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "FLAME WHEEL": { vfx: 'castFire', camera: 'focusAttacker', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'fire', chargeTime: 180, strikeTime: 200, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "FLAMETHROWER": { vfx: 'castFireBeam', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'fire', chargeTime: 200, strikeTime: 200, impactTime: 200, isProjectile: true, isContact: false, screenDim: false },
        "FIRE BLAST": { vfx: 'castFireBlast', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'fire', chargeTime: 250, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "OVERHEAT": { vfx: 'castFireExplosion', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'fire', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Water
        "WATER GUN": { vfx: 'castWater', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'water', chargeTime: 100, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "AQUA JET": { vfx: 'castWaterSplash', camera: 'focusAttacker', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'water', chargeTime: 140, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "SURF": { vfx: 'castWave', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'water', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "HYDRO PUMP": { vfx: 'castWaterBeam', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'water', chargeTime: 280, strikeTime: 250, impactTime: 250, isProjectile: true, isContact: false, screenDim: true },
        "WATER SPOUT": { vfx: 'castWaterExplosion', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'water', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Grass
        "VINE WHIP": { vfx: 'castWhip', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'grass', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "RAZOR LEAF": { vfx: 'castLeaves', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'standard', envFlash: 'grass', chargeTime: 150, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "ENERGY BALL": { vfx: 'castEnergyBall', camera: 'trackProjectile', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'grass', chargeTime: 180, strikeTime: 200, impactTime: 150, isProjectile: true, isContact: false, screenDim: false },
        "LEAF BLADE": { vfx: 'castSlash', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'heavy', envFlash: 'grass', chargeTime: 200, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: true },
        "SOLAR BEAM": { vfx: 'castSolarBeam', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'grass', chargeTime: 400, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Electric
        "THUNDER SHOCK": { vfx: 'castLightning', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'electric', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "SPARK": { vfx: 'castSpark', camera: 'focusAttacker', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'electric', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "THUNDERBOLT": { vfx: 'castLightningBeam', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'standard', envFlash: 'electric', chargeTime: 200, strikeTime: 200, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "THUNDER": { vfx: 'castThunder', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'electric', chargeTime: 250, strikeTime: 250, impactTime: 250, isProjectile: true, isContact: false, screenDim: true },
        "ZAP CANNON": { vfx: 'castElectricExplosion', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'electric', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Ice
        "ICE SHARD": { vfx: 'castIce', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'ice', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "ICY WIND": { vfx: 'castIceWind', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'standard', envFlash: 'ice', chargeTime: 150, strikeTime: 200, impactTime: 150, isProjectile: true, isContact: false, screenDim: false },
        "ICE BEAM": { vfx: 'castIceBeam', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'heavy', envFlash: 'ice', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "BLIZZARD": { vfx: 'castBlizzard', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'ice', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Fighting
        "MACH PUNCH": { vfx: 'castPunch', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'fighting', chargeTime: 80, strikeTime: 100, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "BRICK BREAK": { vfx: 'castChop', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'fighting', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "CROSS CHOP": { vfx: 'castCross', camera: 'focusDefender', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'fighting', chargeTime: 220, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },
        "CLOSE COMBAT": { vfx: 'castFlurry', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'fighting', chargeTime: 300, strikeTime: 300, impactTime: 250, isProjectile: false, isContact: true, screenDim: true },

        // Poison
        "POISON STING": { vfx: 'castSting', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'poison', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "CROSS POISON": { vfx: 'castCross', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'poison', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "SLUDGE BOMB": { vfx: 'castSludge', camera: 'trackProjectile', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'poison', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "GUNK SHOT": { vfx: 'castPoisonExplosion', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'poison', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Ground
        "MUD-SLAP": { vfx: 'castMud', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'ground', chargeTime: 100, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "BULLDOZE": { vfx: 'castQuake', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'ground', chargeTime: 180, strikeTime: 200, impactTime: 150, isProjectile: false, isContact: false, screenDim: false },
        "EARTHQUAKE": { vfx: 'castQuake', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'ground', chargeTime: 250, strikeTime: 250, impactTime: 250, isProjectile: false, isContact: false, screenDim: true },
        "EARTH POWER": { vfx: 'castEarthPower', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'ground', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: false, isContact: false, screenDim: true },

        // Flying
        "PECK": { vfx: 'castPeck', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'flying', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "WING ATTACK": { vfx: 'castWing', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'flying', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "AIR SLASH": { vfx: 'castSlash', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'heavy', envFlash: 'flying', chargeTime: 200, strikeTime: 200, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "BRAVE BIRD": { vfx: 'castBraveBird', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'flying', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: false, isContact: true, screenDim: true },

        // Psychic
        "CONFUSION": { vfx: 'castPsychic', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'psychic', chargeTime: 120, strikeTime: 150, impactTime: 120, isProjectile: true, isContact: false, screenDim: false },
        "PSYBEAM": { vfx: 'castPsybeam', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'psychic', chargeTime: 180, strikeTime: 200, impactTime: 180, isProjectile: true, isContact: false, screenDim: false },
        "PSYCHIC": { vfx: 'castPsychicBurst', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'psychic', chargeTime: 250, strikeTime: 250, impactTime: 250, isProjectile: true, isContact: false, screenDim: true },
        "FUTURE SIGHT": { vfx: 'castFutureSight', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'psychic', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Bug
        "BUG BITE": { vfx: 'castBite', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'bug', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "X-SCISSOR": { vfx: 'castCross', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'bug', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "BUG BUZZ": { vfx: 'castSound', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'bug', chargeTime: 220, strikeTime: 200, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "MEGAHORN": { vfx: 'castHorn', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'bug', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: false, isContact: true, screenDim: true },

        // Rock
        "ROCK THROW": { vfx: 'castRock', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'rock', chargeTime: 120, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "ROCK SLIDE": { vfx: 'castRockTumble', camera: 'wide', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'rock', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "STONE EDGE": { vfx: 'castStoneEdge', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'rock', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: false, isContact: false, screenDim: true },

        // Ghost
        "ASTONISH": { vfx: 'castSpook', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'ghost', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "SHADOW SNEAK": { vfx: 'castShadow', camera: 'focusAttacker', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'ghost', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "SHADOW BALL": { vfx: 'castShadowBall', camera: 'trackProjectile', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'ghost', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "SHADOW CLAW": { vfx: 'castClaw', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'ghost', chargeTime: 250, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },

        // Dragon
        "DRAGON BREATH": { vfx: 'castDragonBeam', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'dragon', chargeTime: 180, strikeTime: 200, impactTime: 150, isProjectile: true, isContact: false, screenDim: false },
        "DRAGON CLAW": { vfx: 'castClaw', camera: 'focusDefender', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'dragon', chargeTime: 200, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },
        "DRACO METEOR": { vfx: 'castMeteor', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'dragon', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },
        "OUTRAGE": { vfx: 'castOutrage', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'dragon', chargeTime: 400, strikeTime: 300, impactTime: 300, isProjectile: false, isContact: true, screenDim: true },

        // Dark
        "BITE": { vfx: 'castBite', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'dark', chargeTime: 100, strikeTime: 120, impactTime: 100, isProjectile: false, isContact: true, screenDim: false },
        "FEINT ATTACK": { vfx: 'castShadow', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'dark', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "CRUNCH": { vfx: 'castBite', camera: 'focusDefender', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'dark', chargeTime: 220, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },
        "DARK PULSE": { vfx: 'castDarkPulse', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'dark', chargeTime: 300, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true },

        // Steel
        "METAL CLAW": { vfx: 'castClaw', camera: 'focusDefender', shakeIntensity: 'normal', lungeStyle: 'standard', envFlash: 'steel', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "FLASH CANNON": { vfx: 'castSteelBeam', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'heavy', envFlash: 'steel', chargeTime: 250, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "IRON HEAD": { vfx: 'castSlam', camera: 'focusDefender', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'steel', chargeTime: 200, strikeTime: 200, impactTime: 200, isProjectile: false, isContact: true, screenDim: true },

        // Fairy
        "DISARMING VOICE": { vfx: 'castSound', camera: 'wide', shakeIntensity: 'light', lungeStyle: 'light', envFlash: 'fairy', chargeTime: 100, strikeTime: 150, impactTime: 100, isProjectile: true, isContact: false, screenDim: false },
        "DRAINING KISS": { vfx: 'castHeart', camera: 'focusAttacker', shakeIntensity: 'light', lungeStyle: 'standard', envFlash: 'fairy', chargeTime: 150, strikeTime: 150, impactTime: 150, isProjectile: false, isContact: true, screenDim: false },
        "DAZZLING GLEAM": { vfx: 'castGleam', camera: 'wide', shakeIntensity: 'normal', lungeStyle: 'heavy', envFlash: 'fairy', chargeTime: 220, strikeTime: 250, impactTime: 200, isProjectile: true, isContact: false, screenDim: true },
        "MOONBLAST": { vfx: 'castMoonblast', camera: 'ultimate', shakeIntensity: 'heavy', lungeStyle: 'ultimate', envFlash: 'fairy', chargeTime: 350, strikeTime: 300, impactTime: 300, isProjectile: true, isContact: false, screenDim: true }
    };

    /**
     * Gets the visual configuration for a specific move.
     * @param {string} moveName - The name of the move.
     * @returns {Object} The configuration object.
     */
    function getConfig(moveName) {
        if (!moveName) return getDefaultForTier('STANDARD', 'normal');
        const normalized = moveName.trim().toUpperCase();
        let m = moves[normalized];
        if (!m) {
            const keys = Object.keys(moves);
            const match = keys.find(k => normalized.includes(k) || k.includes(normalized));
            if (match) m = moves[match];
        }
        
        if (m) {
            return {
                ...m,
                chargeTime: Math.max(m.chargeTime ? Math.round(m.chargeTime * 1.8) : 380, 240),
                strikeTime: Math.max(m.strikeTime ? Math.round(m.strikeTime * 1.4) : 260, 200),
                impactTime: Math.max(m.impactTime ? Math.round(m.impactTime * 1.4) : 220, 180)
            };
        }
        
        return getDefaultForTier('STANDARD', 'normal');
    }

    /**
     * Gets a fallback configuration based on tier and type.
     * @param {string} tier - LIGHT, STANDARD, HEAVY, ULTIMATE
     * @param {string} type - The elemental type
     * @returns {Object} A default configuration object
     */
    function getDefaultForTier(tier = 'STANDARD', type = 'normal') {
        const tierDef = TIERS[tier.toUpperCase()] || TIERS['STANDARD'];
        return {
            vfx: 'castImpact',
            camera: tier === 'ULTIMATE' ? 'ultimate' : 'wide',
            shakeIntensity: tierDef.shakeIntensity,
            lungeStyle: tierDef.lungeStyle,
            envFlash: type.toLowerCase(),
            chargeTime: tierDef.chargeTime,
            strikeTime: tierDef.strikeTime,
            impactTime: tierDef.impactTime,
            isProjectile: false,
            isContact: true,
            screenDim: tierDef.screenDim
        };
    }

    return {
        moves,
        getConfig,
        getDefaultForTier
    };
})();

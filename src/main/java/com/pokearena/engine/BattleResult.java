package com.pokearena.engine;

import java.util.List;

public record BattleResult(
        String winnerName,
        String losserName,
        int roundsPlayed,
        List<String> battleLog
) {
}

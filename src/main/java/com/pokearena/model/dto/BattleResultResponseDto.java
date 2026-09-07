package com.pokearena.model.dto;

import java.util.List;

public record BattleResultResponseDto(
        String winnerTeamName,
        String loserTeamName,
        int roundPlayed,
        List<String> battleLog
) {
}

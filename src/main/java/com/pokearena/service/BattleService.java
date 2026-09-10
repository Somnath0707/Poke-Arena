package com.pokearena.service;

import com.pokearena.engine.BattleEngine;
import com.pokearena.engine.BattlePokemon;
import com.pokearena.engine.BattleResult;
import com.pokearena.entity.BattleHistory;
import com.pokearena.entity.Team;
import com.pokearena.exception.InvalidBattleException;
import com.pokearena.exception.ResourceNotFoundException;
import com.pokearena.model.dto.BattleResultResponseDto;
import com.pokearena.model.dto.BattleSimulationRequest;
import com.pokearena.repository.BattleHistoryRepository;
import com.pokearena.repository.TeamRepository;
import com.pokearena.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BattleService {
    TeamRepository teamRepository;
    TrainerRepository trainerRepository;
    BattleHistoryRepository battleHistoryRepository;
    BattleEngine battleEngine;

    public BattleService(TeamRepository teamRepository, TrainerRepository trainerRepository, BattleHistoryRepository battleHistoryRepository, BattleEngine battleEngine) {
        this.teamRepository = teamRepository;
        this.trainerRepository = trainerRepository;
        this.battleHistoryRepository = battleHistoryRepository;
        this.battleEngine = battleEngine;
    }

    @Transactional
    public BattleResultResponseDto simulateBattle(BattleSimulationRequest request){

        Team teamA = teamRepository.findById(request.teamAId()).orElseThrow(() -> new ResourceNotFoundException("Team A not found"));
        Team teamB = teamRepository.findById(request.teamBId()).orElseThrow(() -> new ResourceNotFoundException("Team B not found"));

        if(teamA.getId().equals(teamB.getId())){
            throw new InvalidBattleException("Cannot battle the same team");
        }

        if (teamA.getTeamMembers().isEmpty() || teamB.getTeamMembers().isEmpty()) {
            throw new InvalidBattleException("Both teams must have at least one Pokémon to battle.");
        }

        BattleResult result = battleEngine.simulate(
                teamA.getTrainer().getName(), mapToBattlePokemon(teamA),
                teamB.getTrainer().getName(), mapToBattlePokemon(teamB)
        );

        if ("Draw".equals(result.winnerName())) {
            // Draw: neither trainer gets a win or a loss
        } else if (result.winnerName().equals(teamA.getTrainer().getName())) {
            teamA.getTrainer().wins();
            teamB.getTrainer().losses();
            trainerRepository.saveAll(List.of(teamA.getTrainer(), teamB.getTrainer()));
        } else {
            teamB.getTrainer().wins();
            teamA.getTrainer().losses();
            trainerRepository.saveAll(List.of(teamA.getTrainer(), teamB.getTrainer()));
        }

        BattleHistory history = new BattleHistory();
        history.setTrainerAName(teamA.getTrainer().getName());
        history.setTrainerBName(teamB.getTrainer().getName());
        history.setWinnerName(result.winnerName());
        history.setRoundPlayed(result.roundsPlayed());
        history.setPlayedAt(LocalDateTime.now());
        history.setBattleLog(result.battleLog());

        battleHistoryRepository.save(history);


        return new BattleResultResponseDto(
                result.winnerName(),
                result.losserName(),
                result.roundsPlayed(),
                result.battleLog()
        );



    }

    private List<BattlePokemon> mapToBattlePokemon(Team team) {
        return team.getTeamMembers().stream()
                .map(m -> new BattlePokemon(
                        m.getSpecies().getName(),
                        m.getSpecies().getPokemonType(),
                        m.getSpecies().getSecondaryType(),
                        m.getSpecies().getBaseHp(),
                        m.getSpecies().getBaseAttack(),
                        m.getSpecies().getBaseDefense(),
                        m.getSpecies().getBaseSpeed(),
                        m.getCurrLevel() // or getCurrentLevel()
                ))
                .toList();
    }

    public List<BattleResultResponseDto> getHistoryForTrainer(String trainerName) {
        return battleHistoryRepository.findByTrainerANameOrTrainerBNameOrderByPlayedAtDesc(trainerName, trainerName)
                .stream()
                .map(h -> new BattleResultResponseDto(
                        h.getWinnerName(),
                        // loser is whichever trainer didn't win:
                        h.getWinnerName().equals(h.getTrainerAName()) ? h.getTrainerBName() : h.getTrainerAName(),
                        h.getRoundPlayed(),
                        h.getBattleLog()
                ))
                .toList();

    }

}

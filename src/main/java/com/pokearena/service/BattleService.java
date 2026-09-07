package com.pokearena.service;

import com.pokearena.engine.BattleEngine;
import com.pokearena.engine.BattlePokemon;
import com.pokearena.engine.BattleResult;
import com.pokearena.entity.BattleHistory;
import com.pokearena.entity.Team;
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

        Team teamA = teamRepository.findById(request.teamAId()).orElseThrow(() -> new RuntimeException("Team A not found"));
        Team teamB = teamRepository.findById(request.teamBId()).orElseThrow(() -> new RuntimeException("Team B not found"));

        if(teamA.getId().equals(teamB.getId())){
            throw new RuntimeException("Cannot battle the same team");
        }

        BattleResult result = battleEngine.simulate(
                teamA.getTrainer().getName(), mapToBattlePokemon(teamA),
                teamB.getTrainer().getName(), mapToBattlePokemon(teamB)
        );

        if(result.winnerName().equals(teamA.getTrainer().getName())){
            teamA.getTrainer().wins();
            teamB.getTrainer().losses();
        } else {
            teamB.getTrainer().wins();
            teamA.getTrainer().losses();
        }

        trainerRepository.saveAll(List.of(teamA.getTrainer(), teamB.getTrainer()));

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
                        m.getSpecies().getBaseHp(),
                        m.getSpecies().getBaseAttack(),
                        m.getSpecies().getBaseDefense(),
                        m.getSpecies().getBaseSpeed(),
                        m.getCurrLevel() // or getCurrentLevel()
                ))
                .toList();
    }

}

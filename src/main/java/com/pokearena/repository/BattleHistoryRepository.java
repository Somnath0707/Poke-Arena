package com.pokearena.repository;

import com.pokearena.entity.BattleHistory;
import com.pokearena.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleHistoryRepository extends JpaRepository<BattleHistory,Long> {
    List<BattleHistory> findByTrainerANameOrTrainerBNameOrderByPlayedAtDesc(String trainerA , String TrainerB );
}

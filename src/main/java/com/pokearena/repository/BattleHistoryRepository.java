package com.pokearena.repository;

import com.pokearena.entity.BattleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleHistoryRepository extends JpaRepository<BattleHistory,Long> {
}

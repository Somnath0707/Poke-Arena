package com.pokearena.repository;

import com.pokearena.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    List<Trainer> findTop10ByOrderByWinsDesc();
}

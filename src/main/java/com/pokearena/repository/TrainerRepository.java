package com.pokearena.repository;

import com.pokearena.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    Optional<Trainer> findByName(String name);
    List<Trainer> findTop10ByOrderByWinsDesc();
}

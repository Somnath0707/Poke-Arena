package com.pokearena.controller;

import com.pokearena.entity.BattleHistory;
import com.pokearena.model.dto.CreateTrainerRequest;
import com.pokearena.model.dto.TrainerResponseDto;
import com.pokearena.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainerController {
    TrainerService trainerService;

    public TrainerController(TrainerService trainerService){
        this.trainerService = trainerService;
    }

    @PostMapping("/api/trainers")
    public ResponseEntity<TrainerResponseDto> createTrainer(@Valid @RequestBody CreateTrainerRequest request){
        TrainerResponseDto response = trainerService.createTrainer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/api/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        return ResponseEntity.ok(trainerService.getLeaderBoard());
    }


}

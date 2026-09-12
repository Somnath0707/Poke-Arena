package com.pokearena.controller;

import com.pokearena.model.dto.BattleResultResponseDto;
import com.pokearena.model.dto.BattleSimulationRequest;
import com.pokearena.repository.BattleHistoryRepository;
import com.pokearena.service.BattleService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Battle Simulation", description = "Simulate turn-based battles and view history")
@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<BattleResultResponseDto> simulate(
            @Valid @RequestBody BattleSimulationRequest request
    ) {
        BattleResultResponseDto response =
                battleService.simulateBattle(request);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/history/{trainerName}")
    public ResponseEntity<List<BattleResultResponseDto>> getBattleHistory(@PathVariable String trainerName){
        List<BattleResultResponseDto> history = battleService.getHistoryForTrainer(trainerName);
        return ResponseEntity.ok(history);
    }
}
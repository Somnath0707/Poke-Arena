package com.pokearena.controller;

import com.pokearena.model.dto.BattleResultResponseDto;
import com.pokearena.model.dto.BattleSimulationRequest;
import com.pokearena.repository.BattleHistoryRepository;
import com.pokearena.service.BattleService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            @Valid @RequestBody BattleSimulationRequest request,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        BattleResultResponseDto response =
                battleService.simulateBattle(request, username);

        return ResponseEntity.ok(response);
    }


    @GetMapping({"/history/{trainerName}", "/history"})
    public ResponseEntity<List<BattleResultResponseDto>> getBattleHistory(
            @PathVariable(required = false) String trainerName,
            @AuthenticationPrincipal UserDetails userDetails) {
        String targetTrainer = (trainerName != null && !trainerName.isBlank())
                ? trainerName
                : (userDetails != null ? userDetails.getUsername() : null);
        List<BattleResultResponseDto> history = battleService.getHistoryForTrainer(targetTrainer);
        return ResponseEntity.ok(history);
    }
}
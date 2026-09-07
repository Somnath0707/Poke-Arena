package com.pokearena.controller;

import com.pokearena.model.dto.BattleResultResponseDto;
import com.pokearena.model.dto.BattleSimulationRequest;
import com.pokearena.service.BattleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
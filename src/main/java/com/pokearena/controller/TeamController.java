package com.pokearena.controller;

import com.pokearena.model.dto.CreateTeamRequest;
import com.pokearena.model.dto.TeamMemberResponseDto;
import com.pokearena.model.dto.TeamResponseDto;
import com.pokearena.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Team Management", description = "Create and configure 3-Pokemon battle teams")
@RestController
public class TeamController {
    TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/api/teams")
    public ResponseEntity<TeamResponseDto> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        TeamResponseDto response = teamService.createTeam(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

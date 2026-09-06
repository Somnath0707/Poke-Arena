package com.pokearena.service;

import com.pokearena.entity.PokemonSpecies;
import com.pokearena.entity.Team;
import com.pokearena.entity.TeamMember;
import com.pokearena.entity.Trainer;
import com.pokearena.model.dto.CreateTeamMemberRequest;
import com.pokearena.model.dto.CreateTeamRequest;
import com.pokearena.model.dto.TeamMemberResponseDto;
import com.pokearena.model.dto.TeamResponseDto;
import com.pokearena.repository.PokemonSpeciesRepository;
import com.pokearena.repository.TeamRepository;
import com.pokearena.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final PokemonSpeciesRepository pokemonSpeciesRepository;
    private final TeamRepository teamRepository;
    private final TrainerRepository trainerRepository;

    public TeamService(PokemonSpeciesRepository pokemonSpeciesRepository, TeamRepository teamRepository, TrainerRepository trainerRepository) {
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
        this.teamRepository = teamRepository;
        this.trainerRepository = trainerRepository;
    }


    public TeamResponseDto createTeam(CreateTeamRequest request) {
        Trainer trainer = trainerRepository.findById(request.trainerId()).
                orElseThrow(() -> new RuntimeException("Trainer not found with id: " + request.trainerId()));

        Team team = new Team();
        team.setTrainer(trainer);
        team.setName(request.teamName());

        for(CreateTeamMemberRequest memberReq : request.members()){
            PokemonSpecies species = pokemonSpeciesRepository.findById(memberReq.speciesId()).
                    orElseThrow(()-> new RuntimeException("Pokemon species not found with id: " + memberReq.speciesId()));


            TeamMember member = new TeamMember();
            member.setSpecies(species);
            member.setCurrLevel(memberReq.level());
            member.setSlotNumber(memberReq.slotOrder());

            team.addTeamMember(member);
        }

        Team savedTeam = teamRepository.save(team);

        List<TeamMemberResponseDto> memberDtos = savedTeam.getTeamMembers().stream()
                .map(m-> new TeamMemberResponseDto(
                        m.getId(),
                        m.getSpecies().getName(),
                        m.getSpecies().getPokemonType(),
                        m.getCurrLevel(),
                        m.getSlotNumber()
                )).toList();

        return new TeamResponseDto(
                savedTeam.getId(),
                savedTeam.getName(),
                trainer.getId(),
                trainer.getName(),
                memberDtos
        );

    }
}

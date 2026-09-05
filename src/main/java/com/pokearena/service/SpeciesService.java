package com.pokearena.service;

import com.pokearena.model.dto.SpeciesResponse;
import com.pokearena.entity.PokemonSpecies;
import com.pokearena.repository.PokemonSpeciesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpeciesService {

    PokemonSpeciesRepository pokemonSpeciesRepository;

    public SpeciesService(PokemonSpeciesRepository pokemonSpeciesRepository){
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
    }

    public List<SpeciesResponse> getAllSpecies() {
        List<PokemonSpecies> speciesList = pokemonSpeciesRepository.findAll();
        List<SpeciesResponse> responseList = new ArrayList<>();
        for(PokemonSpecies species : speciesList){
            SpeciesResponse response = new SpeciesResponse(
                    species.getId(),
                    species.getName(),
                    species.getPokemonType(),
                    species.getSecondaryType(),
                    species.getBaseHp(),
                    species.getBaseAttack(),
                    species.getBaseDefense(),
                    species.getBaseSpeed()
            );

            responseList.add(response);
        }
        return responseList;
    }
}

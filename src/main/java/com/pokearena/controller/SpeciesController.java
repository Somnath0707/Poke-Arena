package com.pokearena.controller;

import com.pokearena.model.dto.SpeciesResponse;
import com.pokearena.service.SpeciesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Pokemon Species", description = "Query Pokemon species and base stats")
@RestController
public class SpeciesController {
    SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService){
        this.speciesService = speciesService;
    }

    @GetMapping("/api/species")
    public List<SpeciesResponse> getAllSpecies() {
        return speciesService.getAllSpecies();
    }




}

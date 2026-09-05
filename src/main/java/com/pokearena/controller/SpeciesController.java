package com.pokearena.controller;

import com.pokearena.model.dto.SpeciesResponse;
import com.pokearena.service.SpeciesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpeciesController {
    SpeciesService speciesService;



    public SpeciesController(SpeciesService speciesService){
        this.speciesService = speciesService;
    }

    @RequestMapping("/api/species")
    public List<SpeciesResponse> getAllSpecies() {
        return speciesService.getAllSpecies();
    }




}

package com.pokearena.repository;

import com.pokearena.entity.PokemonSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
// so we do is Entity and then Id
public interface PokemonSpeciesRepository extends JpaRepository<PokemonSpecies, Long> {
}

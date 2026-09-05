package com.pokearena.config;
import java.util.*;

import com.pokearena.model.PokemonType;
import com.pokearena.entity.PokemonSpecies;
import com.pokearena.repository.PokemonSpeciesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class DataSeeder implements CommandLineRunner {
    private final PokemonSpeciesRepository pokemonSpeciesRepository;

    public DataSeeder(PokemonSpeciesRepository pokemonSpeciesRepository) {
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        if(pokemonSpeciesRepository.count() == 0) {
            seedData();
        }
        // Seed data here
    }



    private PokemonSpecies createSpecies(
            String name,
            PokemonType type1,
            PokemonType type2,
            int baseHp,
            int baseAttack,
            int baseDefense,
            int baseSpeed
    ){
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(name);
        pokemonSpecies.setPokemonType(type1);
        if(type2 != null) {
            pokemonSpecies.setSecondaryType(type2);
        }
        pokemonSpecies.setBaseHp(baseHp);
        pokemonSpecies.setBaseAttack(baseAttack);
        pokemonSpecies.setBaseDefense(baseDefense);
        pokemonSpecies.setBaseSpeed(baseSpeed);

        return pokemonSpecies;
    }



    public void seedData() {
        PokemonSpecies pikachu = createSpecies("Pikachu", PokemonType.Electric, null, 35, 55, 40, 90);
        PokemonSpecies charmander = createSpecies("Charmander", PokemonType.Fire, null, 39, 52, 43, 65);
        PokemonSpecies squirtle = createSpecies("Squirtle", PokemonType.Water, null, 44, 48, 65, 43);
        PokemonSpecies bulbasaur = createSpecies("Bulbasaur", PokemonType.Grass, PokemonType.Poison, 45, 49, 49, 45);

        System.out.println(pikachu.getName() + " Makes sound Of " + pikachu.getPokemonType());
        pokemonSpeciesRepository.saveAll(
                List.of(
                        pikachu,
                        charmander,
                        squirtle,
                        bulbasaur
                )
        );



    }


}
package com.pokearena.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.pokearena.model.PokemonType;
import com.pokearena.entity.PokemonSpecies;
import com.pokearena.repository.PokemonSpeciesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PokemonSpeciesRepository pokemonSpeciesRepository;

    public DataSeeder(PokemonSpeciesRepository pokemonSpeciesRepository) {
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Idempotency check: Only seed if the database table is completely empty
        if (pokemonSpeciesRepository.count() == 0) {
            seedData();
        }
    }

    private PokemonSpecies createSpecies(
            String name,
            PokemonType type1,
            PokemonType type2,
            int baseHp,
            int baseAttack,
            int baseDefense,
            int baseSpeed
    ) {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(name);
        pokemonSpecies.setPokemonType(type1);
        if (type2 != null) {
            pokemonSpecies.setSecondaryType(type2);
        }
        pokemonSpecies.setBaseHp(baseHp);
        pokemonSpecies.setBaseAttack(baseAttack);
        pokemonSpecies.setBaseDefense(baseDefense);
        pokemonSpecies.setBaseSpeed(baseSpeed);

        return pokemonSpecies;
    }

    public void seedData() {
        List<PokemonSpecies> speciesList = new ArrayList<>();

        // Try-with-resources: automatically closes the stream when finished
        try (InputStream is = getClass().getResourceAsStream("/Pokemon.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                System.err.println("Could not find /Pokemon.csv in resources!");
                return;
            }

            // 1. Skip the header row ("ID","Name","Form",...)
            String header = reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Strip all double-quotes
                line = line.replace("\"", "");
                String[] tokens = line.split(",");

                // 2. Handle Name and Form (Prevents database unique constraint violation)
                String baseName = tokens[1].trim();
                String form = tokens[2].trim();
                String finalName = form.isEmpty() ? baseName : baseName + " (" + form + ")";

                // 3. Parse Primary Type
                PokemonType primaryType = PokemonType.valueOf(tokens[3].trim());

                // 4. Parse Secondary Type (null if empty or blank)
                PokemonType secondaryType = null;
                String secStr = tokens[4].trim();
                if (!secStr.isEmpty()) {
                    secondaryType = PokemonType.valueOf(secStr);
                }

                // 5. Parse Base Stats
                int hp = Integer.parseInt(tokens[6].trim());
                int attack = Integer.parseInt(tokens[7].trim());
                int defense = Integer.parseInt(tokens[8].trim());
                int speed = Integer.parseInt(tokens[11].trim());

                // 6. Build the entity and add to our batch list
                PokemonSpecies species = createSpecies(
                        finalName, primaryType, secondaryType, hp, attack, defense, speed
                );
                speciesList.add(species);
            }

            // 7. Bulk save all 1,215 records in a single database transaction
            pokemonSpeciesRepository.saveAll(speciesList);
            System.out.println("✅ Successfully seeded " + speciesList.size() + " Pokémon from CSV into H2!");

        } catch (Exception e) {
            System.err.println("Error seeding Pokémon data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
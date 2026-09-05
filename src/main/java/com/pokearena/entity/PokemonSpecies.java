package com.pokearena.entity;

import com.pokearena.model.PokemonType;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "pokemon_species")
public class PokemonSpecies {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private PokemonType pokemonType;

    @Enumerated(EnumType.STRING)
    private PokemonType secondaryType;

    private int baseHp;
    private int baseAttack;
    private int baseDefense;
    private int baseSpeed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(PokemonType pokemonType) {
        this.pokemonType = pokemonType;
    }

    public PokemonType getSecondaryType() {
        return secondaryType;
    }

    public void setSecondaryType(PokemonType secondaryType) {
        this.secondaryType = secondaryType;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }
}

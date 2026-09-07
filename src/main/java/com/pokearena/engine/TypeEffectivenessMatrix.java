package com.pokearena.engine;

import com.pokearena.model.PokemonType;
import org.springframework.stereotype.Component;

@Component
public class TypeEffectivenessMatrix {

    public double getMultiplier(PokemonType attackType , PokemonType defenseType){

        if(attackType == PokemonType.Fire && defenseType == PokemonType.Grass ){
            return 2.0;
        }
        if(attackType == PokemonType.Fire && defenseType == PokemonType.Water ){
            return 0.5;
        }
        if(attackType == PokemonType.Fire && defenseType == PokemonType.Fire ){
            return 0.5;
        }
        if(attackType == PokemonType.Water && defenseType == PokemonType.Fire){
            return 2.0;
        }
        if(attackType == PokemonType.Water && defenseType == PokemonType.Grass){
            return 0.5;
        }
        if(attackType == PokemonType.Water && defenseType == PokemonType.Water){
            return 0.5;
        }

        if(attackType == PokemonType.Grass && defenseType == PokemonType.Water){
            return 2.0;
        }
        if(attackType == PokemonType.Grass && defenseType ==  PokemonType.Fire){
            return 0.5;
        }
        if(attackType == PokemonType.Grass && defenseType == PokemonType.Grass){
            return 0.5;
        }

        if(attackType == PokemonType.Electric && defenseType == PokemonType.Water ){
            return 2.0;
        }

        if(attackType == PokemonType.Electric && defenseType == PokemonType.Electric){
            return 0.5;
        }

        if (attackType == PokemonType.Electric && defenseType == PokemonType.Ground) {
            return 0.0;
        }



        else return 1.0;


    }
}

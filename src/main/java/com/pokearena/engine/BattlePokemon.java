package com.pokearena.engine;

import com.pokearena.model.PokemonType;

public class BattlePokemon {
    private  String name;
    private PokemonType type;
    private int maxHp;
    private int currHp;
    private int attack;
    private int defence;
    private int speed;
    private int level;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonType getType() {
        return type;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public BattlePokemon(String name, PokemonType type, int baseHp, int baseAttack, int baseDefence, int baseSpeed, int level) {
        this.name = name;
        this.type = type;
        this.level = level;


        this.maxHp = baseHp + (2 * level);
        this.currHp = this.maxHp;
        this.attack = baseAttack + level;
        this.defence = baseDefence + level;
        this.speed = baseSpeed + level;
    }

    public boolean isFainted(){
        return currHp<=0;
    }

    public void takeDamage(int damage){
        currHp = Math.max(0 , currHp - damage);
    }



}

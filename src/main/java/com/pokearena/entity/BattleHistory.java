package com.pokearena.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name =  "battle_histories")
public class BattleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String trainerAName;
    String trainerBName;
    String winnerName;
    int roundPlayed;
    LocalDateTime playedAt;
    @ElementCollection
    private List<String> battleLog = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainerAName() {
        return trainerAName;
    }

    public void setTrainerAName(String trainerAName) {
        this.trainerAName = trainerAName;
    }

    public String getTrainerBName() {
        return trainerBName;
    }

    public void setTrainerBName(String trainerBName) {
        this.trainerBName = trainerBName;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getRoundPlayed() {
        return roundPlayed;
    }

    public void setRoundPlayed(int roundPlayed) {
        this.roundPlayed = roundPlayed;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(List<String> battleLog) {
        this.battleLog = battleLog;
    }
}

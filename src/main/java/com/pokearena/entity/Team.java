package com.pokearena.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    // cascade = CascadeType.ALL means that any operation (persist, merge, remove, etc.) performed on the Team entity will be cascaded to its associated TeamMember entities. This ensures that when a Team is saved or deleted, its related TeamMembers are also saved or deleted accordingly.
    // orphanRemoval = true means that if a TeamMember is removed from the teamMembers list, it will be automatically deleted from the database. This is useful for maintaining data integrity and ensuring that there are no orphaned TeamMember records that are no longer associated with any Team.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> members = new ArrayList<>();

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

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public List<TeamMember> getTeamMembers() {
        return members;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.members = teamMembers;
    }

    public void addTeamMember(TeamMember member){
        members.add(member);
        member.setTeam(this);// Set the team reference in the TeamMember entity
    }
}

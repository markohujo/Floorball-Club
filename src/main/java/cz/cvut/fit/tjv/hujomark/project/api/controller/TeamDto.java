package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;

import java.util.HashSet;
import java.util.Set;

public class TeamDto {
    public Long id;

    public String name;

    public Set<Player> players;

    public Set<Match> matches;

    public TeamDto() {}

    public TeamDto(Long id, String name, Set<Player> players, Set<Match> matches) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.matches = matches;
    }

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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }
}

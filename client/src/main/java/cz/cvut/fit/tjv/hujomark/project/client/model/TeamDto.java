package cz.cvut.fit.tjv.hujomark.project.client.model;

import java.util.Set;

public class TeamDto {
    public Long id;

    public String name;

    public Set<Long> players;

    public Set<Long> matches;

    public Long tmpId;

    public TeamDto() {}

    public TeamDto(Long id, String name, Set<Long> players, Set<Long> matches) {
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

    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public Set<Long> getMatches() {
        return matches;
    }

    public void setMatches(Set<Long> matches) {
        this.matches = matches;
    }

    public Long getTmpId() {
        return tmpId;
    }

    public void setTmpId(Long tmpId) {
        this.tmpId = tmpId;
    }
}

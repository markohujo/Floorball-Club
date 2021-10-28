package cz.cvut.fit.tjv.hujomark.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {
    @Id
    private Long id;

    private String name;

    @ManyToMany
    private final Set<Player> players = new HashSet<>();

    @OneToMany
    private final Set<Match> matches = new HashSet<>();

    public Team() {}

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Set<Match> getMatches() {
        return matches;
    }
}

package cz.cvut.fit.tjv.hujomark.project.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Team {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq")
    @SequenceGenerator(name = "team_seq", sequenceName = "team_seq", allocationSize = 1)
    @Id
    private Long id;

    private String name;

    // TODO
    @ManyToMany
    private Set<Player> players;

    // TODO
    @OneToMany
    private Set<Match> matches;

    public Team() {}

    public Team(Long id, String name, Set<Player> players, Set<Match> matches) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.matches = matches;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void removePlayer(Player player) {
        players.remove(player);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", players=" + players +
            ", matches=" + matches +
            '}';
    }
}

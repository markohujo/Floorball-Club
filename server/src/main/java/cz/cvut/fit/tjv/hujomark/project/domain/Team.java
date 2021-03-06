package cz.cvut.fit.tjv.hujomark.project.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Team {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "player_team",
               joinColumns = @JoinColumn(name = "team_id"),
               inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players;

    @OneToMany(mappedBy = "team")
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

    public void removeMatch(Match match) {
        matches.remove(match);
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

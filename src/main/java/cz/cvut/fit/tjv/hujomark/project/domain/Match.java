package cz.cvut.fit.tjv.hujomark.project.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Match {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq")
    @SequenceGenerator(name = "match_seq", sequenceName = "match_seq", allocationSize = 1)
    @Id
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Match() {}

    public Match(Long id, LocalDateTime dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }

    public Match(Long id, LocalDateTime dateTime, Team team) {
        this.id = id;
        this.dateTime = dateTime;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match match)) return false;
        return id.equals(match.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Match{" +
            "id=" + id +
            ", dateTime=" + dateTime +
            ", team=" + team +
            '}';
    }
}

package cz.cvut.fit.tjv.hujomark.project.api.converter;

import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamDto;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

public class TeamConverter {

    public static Team toModel(TeamDto teamDto) {
        // empty HashSet is used as this method is only used when creating a new team
        // (obviously with an empty set of players and matches)
        return new Team(teamDto.id, teamDto.name, new HashSet<>(), new HashSet<>());
    }

    public static TeamDto fromModel(Team team) {
        return new TeamDto(
            team.getId(),
            team.getName(),
            team.getPlayers().stream().map(Player::getId).collect(toSet()),
            team.getMatches().stream().map(Match::getId).collect(toSet())
        );
    }

    public static Collection<Team> toModelMany(Collection<TeamDto> teamDtos) {
        Collection<Team> teams = new ArrayList<>();
        teamDtos.forEach(teamDto -> teams.add(toModel(teamDto)));
        return teams;
    }

    public static Collection<TeamDto> fromModelMany(Collection<Team> teams) {
        Collection<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(team -> teamDtos.add(fromModel(team)));
        return teamDtos;
    }
}

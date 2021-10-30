package cz.cvut.fit.tjv.hujomark.project.api.converter;

import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamDto;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;

import java.util.ArrayList;
import java.util.Collection;

public class TeamConverter {
    public static Team toModel(TeamDto teamDto) {
        return new Team(teamDto.id, teamDto.name, teamDto.players, teamDto.matches);
    }

    public static TeamDto fromModel(Team team) {
        return new TeamDto(team.getId(),team.getName(), team.getPlayers(), team.getMatches());
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

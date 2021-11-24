package cz.cvut.fit.tjv.hujomark.project.api.converter;

import cz.cvut.fit.tjv.hujomark.project.api.controller.MatchDto;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class MatchConverter {
    private static TeamService teamService;

    public MatchConverter(TeamService teamService) {
        MatchConverter.teamService = teamService;
    }

    public static Match toModel(MatchDto matchDto) {
        return new Match(matchDto.id, matchDto.dateTime, teamService.readById(matchDto.teamId).orElseThrow());
    }

    public static MatchDto fromModel(Match match) {
        return new MatchDto(match.getId(), match.getDateTime(), match.getTeam().getId());
    }

    public static Collection<Match> toModelMany(Collection<MatchDto> MatchDtos) {
        Collection<Match> matches = new ArrayList<>();
        MatchDtos.forEach(matchDto -> matches.add(toModel(matchDto)));
        return matches;
    }

    public static Collection<MatchDto> fromModelMany(Collection<Match> matches) {
        Collection<MatchDto> MatchDtos = new ArrayList<>();
        matches.forEach(match -> MatchDtos.add(fromModel(match)));
        return MatchDtos;
    }
}

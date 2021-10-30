package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST API for Team entity
 *
 * Supported methods:
 *      POST newTeam (/teams)
 *      GET all (/teams)
 *      GET one (/teams/{id})
 *      GET players (/teams/{id}/players)
 *      GET matches (/teams/{id}/matches)
 *      PUT addPlayer (/teams/{id})
 *      PUT addMatch (/teams/{id})
 *      PUT removePlayer (/teams/{id})
 *      DELETE deleteTeam (/teams/{id})
 */
@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/teams")
    public TeamDto newMatch(@RequestBody TeamDto newTeam) {
        Team team = TeamConverter.toModel(newTeam);
        teamService.create(team);
        return TeamConverter.fromModel(team);
    }

    @GetMapping("/teams")
    public Collection<TeamDto> all() {
        return TeamConverter.fromModelMany(teamService.readAll());
    }

    @GetMapping("/teams/{id}")
    public TeamDto one(@PathVariable Long id) {
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @GetMapping("/teams/{id}/players")
    public Collection<PlayerDto> teams(@PathVariable Long id) {
        Team team = teamService.readById(id).orElseThrow();
        return PlayerConverter.fromModelMany(team.getPlayers());
    }

    @GetMapping("/teams/{id}/matches")
    public Collection<MatchDto> matches(@PathVariable Long id) {
        Team team = teamService.readById(id).orElseThrow();
        return MatchConverter.fromModelMany(team.getMatches());
    }

    @PutMapping("/teams/{id}/players")
    public TeamDto addPlayer(Long id, PlayerDto playerDto) {
        teamService.addPlayer(id, PlayerConverter.toModel(playerDto));
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @PutMapping("/teams/{id}/matches")
    public TeamDto addMatch(Long id, MatchDto matchDto) {
        teamService.addMatch(id, MatchConverter.toModel(matchDto));
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    public TeamDto removePlayer(Long id, PlayerDto playerDto) {
        teamService.removePlayer(id, PlayerConverter.toModel(playerDto));
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @DeleteMapping("/matches/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}

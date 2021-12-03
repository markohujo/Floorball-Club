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
 *      PUT addOrRemovePlayer (/teams/{id}/players)
 *      PUT addOrRemoveMatch (/teams/{id}/matches)
 *      DELETE deleteTeam (/teams/{id})
 */
@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/teams")
    public TeamDto newMatch(@RequestBody TeamDto newTeamDTO) {
        return TeamConverter.fromModel(teamService.create(TeamConverter.toModel(newTeamDTO)));
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

    @PutMapping("/teams/{id}/players/add")
    public TeamDto addPlayer(@PathVariable Long id, @RequestParam Long player) {
        teamService.addPlayer(id, player);
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @PutMapping("/teams/{id}/players/remove")
    public TeamDto removePlayer(@PathVariable Long id, @RequestParam Long player) {
        teamService.removePlayer(id, player);
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @PutMapping("/teams/{id}/matches/add")
    public TeamDto addMatch(@PathVariable Long id, @RequestParam Long match) {
        teamService.addMatch(id, match);
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @PutMapping("/teams/{id}/matches/remove")
    public TeamDto removeMatch(@PathVariable Long id, @RequestParam Long match) {
        teamService.removeMatch(id, match);
        return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
    }

    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}

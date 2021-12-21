package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

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
        Team team = teamService.create(TeamConverter.toModel(newTeamDTO));
        return one(team.getId());
    }

    @GetMapping("/teams")
    public Collection<TeamDto> all() {
        return TeamConverter.fromModelMany(teamService.readAll());
    }

    @GetMapping("/teams/{id}")
    public TeamDto one(@PathVariable Long id) {
        return TeamConverter.fromModel(teamService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Not Found")));
    }

    @GetMapping("/teams/{id}/players")
    public Collection<PlayerDto> teams(@PathVariable Long id) {
        Team team = teamService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Not Found"));
        return PlayerConverter.fromModelMany(team.getPlayers());
    }

    @GetMapping("/teams/{id}/matches")
    public Collection<MatchDto> matches(@PathVariable Long id) {
        Team team = teamService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Not Found"));
        return MatchConverter.fromModelMany(team.getMatches());
    }

    @PutMapping("/teams/{id}/players/add")
    public TeamDto addPlayer(@PathVariable Long id, @RequestParam Long player) {
        try {
            teamService.addPlayer(id, player);
            return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/teams/{id}/players/remove")
    public TeamDto removePlayer(@PathVariable Long id, @RequestParam Long player) {
        try {
            teamService.removePlayer(id, player);
            return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/teams/{id}/matches/add")
    public TeamDto addMatch(@PathVariable Long id, @RequestParam Long match) {
        try {
            teamService.addMatch(id, match);
            return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/teams/{id}/matches/remove")
    public TeamDto removeMatch(@PathVariable Long id, @RequestParam Long match) {
        try {
            teamService.removeMatch(id, match);
            return TeamConverter.fromModel(teamService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Long id) {
        try {
            teamService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Not Found");
        }
    }
}

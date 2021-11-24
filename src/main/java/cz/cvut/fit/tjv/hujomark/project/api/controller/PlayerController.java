package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST API for Player entity
 *
 * Supported methods:
 *      POST newPlayer (/players)
 *      GET all (/players)
 *      GET one (/players/{id})
 *      GET teams (/players/{id}/teams)
 *      GET matches (/players/{id}/matches)
 *      PUT updateEmail (/players/{id})
 *      PUT addToTeam (/players/{id}/addTeam)
 *      PUT removeFromTeam (/players/{id}/removeTeam)
 *      DELETE deletePlayer (/players/{id})
 */
@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/players")
    public PlayerDto newPlayer(@RequestBody PlayerDto newPlayerDTO) {
        return PlayerConverter.fromModel(playerService.create(PlayerConverter.toModel(newPlayerDTO)));
    }

    @GetMapping("/players")
    public Collection<PlayerDto> all() {
        return PlayerConverter.fromModelMany(playerService.readAll());
    }

    @GetMapping("/players/{id}")
    public PlayerDto one(@PathVariable Long id) {
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
    }

    @GetMapping("/players/{id}/teams")
    public Collection<TeamDto> teams(@PathVariable Long id) {
        return TeamConverter.fromModelMany(playerService.findTeams(id));
    }

    @GetMapping("/players/{id}/matches")
    public Collection<MatchDto> matches(@PathVariable Long id) {
        return MatchConverter.fromModelMany(playerService.findMatches(id));
    }

    @PutMapping("/players/{id}")
    public PlayerDto updateEmail(@PathVariable Long id, @RequestParam String email) {
        playerService.updateEmail(id, email);
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
    }

    @PutMapping("/players/{id}/addTeam")
    public PlayerDto addToTeam(@PathVariable Long id, @RequestParam Long teamId) {
        playerService.addToTeam(id, teamId);
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
    }

    @PutMapping("/players/{id}/removeTeam")
    public PlayerDto removeFromTeam(@PathVariable Long id, @RequestParam Long teamId) {
        playerService.removeFromTeam(id, teamId);
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
    }
}

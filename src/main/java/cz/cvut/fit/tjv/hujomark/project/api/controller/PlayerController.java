package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;

import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

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
        Player player = playerService.create(PlayerConverter.toModel(newPlayerDTO));
        return one(player.getId());
    }

    @GetMapping("/players")
    public Collection<PlayerDto> all() {
        return PlayerConverter.fromModelMany(playerService.readAll());
    }

    @GetMapping("/players/{id}")
    public PlayerDto one(@PathVariable Long id) {
        return PlayerConverter.fromModel(playerService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found")));
    }

    @GetMapping("/players/{id}/teams")
    public Collection<TeamDto> teams(@PathVariable Long id) {
        if (playerService.readById(id).isPresent())
            return TeamConverter.fromModelMany(playerService.findTeams(id));
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
    }

    @GetMapping("/players/{id}/matches")
    public Collection<MatchDto> matches(@PathVariable Long id) {
        if (playerService.readById(id).isPresent())
            return MatchConverter.fromModelMany(playerService.findMatches(id));
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
    }

    @PutMapping("/players/{id}")
    public PlayerDto updateEmail(@PathVariable Long id, @RequestParam String email) {
        try {
            playerService.updateEmail(id, email);
            return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
        }
    }

    @PutMapping("/players/{id}/teams/add")
    public PlayerDto addToTeam(@PathVariable Long id, @RequestParam Long teamId) {
        try {
            playerService.addToTeam(id, teamId);
            return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/players/{id}/teams/remove")
    public PlayerDto removeFromTeam(@PathVariable Long id, @RequestParam Long teamId) {
        try {
            playerService.removeFromTeam(id, teamId);
            return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        try {
            playerService.deletePlayerById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
        }
    }
}

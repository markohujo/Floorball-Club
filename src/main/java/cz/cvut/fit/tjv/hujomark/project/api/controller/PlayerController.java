package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;

import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    private final TeamService teamService;

    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    // TODO does not save new player to db
    @PostMapping("/players")
    public PlayerDto newPlayer(@RequestBody PlayerDto newPlayer) {
        Player player = PlayerConverter.toModel(newPlayer);
        playerService.create(player);
        return PlayerConverter.fromModel(player);
    }

    @GetMapping("/players")
    public Collection<PlayerDto> all() {
        return PlayerConverter.fromModelMany(playerService.readAll());
    }

    @GetMapping("/players/{id}")
    public PlayerDto one(@PathVariable Long id) {
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow(IllegalArgumentException::new));
    }

    /**
     * Iterates over all teams and determines whether each team contains player with the given id
     */
    @GetMapping("/players/{id}/teams")
    public Collection<TeamDto> teams(@PathVariable Long id) {
        Set<Team> teams = new HashSet<>();
        teamService.readAll().forEach(team -> {
            if (team.getPlayers().contains(playerService.readById(id).orElseThrow()))
                teams.add(team);
        });
        return TeamConverter.fromModelMany(teams);
    }

    @PutMapping("/players/{id}")
    public PlayerDto updateEmail(@PathVariable Long id, @RequestParam String email) {
        playerService.updateEmail(id, email);
        return PlayerConverter.fromModel(playerService.readById(id).orElseThrow());
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
    }
}

package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.PlayerConverter;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;

import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
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

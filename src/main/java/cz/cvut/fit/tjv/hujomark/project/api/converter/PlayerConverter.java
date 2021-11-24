package cz.cvut.fit.tjv.hujomark.project.api.converter;

import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

public class PlayerConverter {
    public static Player toModel(PlayerDto playerDto) {
        // empty HashSet is used as this method is only used when creating a new player
        // (obviously with an empty set of teams)
        return new Player(playerDto.id, playerDto.name, playerDto.surname, playerDto.email, playerDto.dateOfBirth, new HashSet<>());
    }

    public static PlayerDto fromModel(Player player) {
        return new PlayerDto(
            player.getId(),player.getName(), player.getSurname(), player.getEmail(), player.getDateOfBirth(),
            player.getTeams().stream().map(Team::getId).collect(toSet()));
    }

    public static Collection<Player> toModelMany(Collection<PlayerDto> playerDtos) {
        Collection<Player> players = new ArrayList<>();
        playerDtos.forEach(playerDto -> players.add(toModel(playerDto)));
        return players;
    }

    public static Collection<PlayerDto> fromModelMany(Collection<Player> players) {
        Collection<PlayerDto> playerDtos = new ArrayList<>();
        players.forEach(player -> playerDtos.add(fromModel(player)));
        return playerDtos;
    }
}

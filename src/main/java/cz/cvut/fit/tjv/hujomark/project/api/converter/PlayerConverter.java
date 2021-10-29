package cz.cvut.fit.tjv.hujomark.project.api.converter;

import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerConverter {
    public static Player toModel(PlayerDto playerDto) {
        return new Player(playerDto.id, playerDto.name, playerDto.surname, playerDto.email, playerDto.dateOfBirth);
    }

    public static PlayerDto fromModel(Player player) {
        return new PlayerDto(player.getId(),player.getName(), player.getSurname(), player.getEmail(), player.getDateOfBirth());
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

package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {

    @InjectMocks
    TeamService service;

    @Mock
    PlayerService playerService;

    @Mock
    TeamJpaRepository repository;

    @Test
    public void testAddPlayer() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.of(player));
        Mockito.when(repository.existsById(team.getId())).thenReturn(true);
        Mockito.when(repository.save(team)).thenReturn(team);

        Assertions.assertTrue(team.getPlayers().isEmpty());
        service.addPlayer(team.getId(), player.getId());
        Assertions.assertFalse(team.getPlayers().isEmpty());
        Assertions.assertEquals(1, team.getPlayers().size());
        Assertions.assertTrue(team.getPlayers().contains(player));

        Mockito.verify(repository, Mockito.times(1)).save(team);
        Mockito.verify(playerService, Mockito.times(1)).readById(player.getId());
    }

    @Test
    public void testAddPlayerPlayerDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getPlayers().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.addPlayer(team.getId(), player.getId()));
        Assertions.assertTrue(team.getPlayers().isEmpty());

        Mockito.verify(repository, Mockito.never()).save(team);
        Mockito.verify(playerService, Mockito.times(1)).readById(player.getId());
    }

    @Test
    public void testAddPlayerTeamDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getPlayers().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.addPlayer(team.getId(), player.getId()));
        Assertions.assertTrue(team.getPlayers().isEmpty());

        Mockito.verify(repository, Mockito.never()).save(team);
        Mockito.verify(playerService, Mockito.never()).readById(player.getId());
    }
}

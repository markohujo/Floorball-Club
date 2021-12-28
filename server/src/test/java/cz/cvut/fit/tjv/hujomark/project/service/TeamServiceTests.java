package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.AbstractCrudService;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
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

    @Mock
    private MatchService matchService;

    @Test
    public void testAddPlayer() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.of(player));

        Assertions.assertTrue(team.getPlayers().isEmpty());
        service.addPlayer(team.getId(), player.getId());
        Assertions.assertFalse(team.getPlayers().isEmpty());
        Assertions.assertEquals(1, team.getPlayers().size());
        Assertions.assertTrue(team.getPlayers().contains(player));

        Mockito.verify(playerService, Mockito.times(1)).readById(player.getId());
    }

    @Test
    public void testAddPlayerPlayerDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getPlayers().isEmpty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () -> service.addPlayer(team.getId(), player.getId()));
        Assertions.assertEquals("Player Not Found", e.getMessage());
        Assertions.assertTrue(team.getPlayers().isEmpty());

        Mockito.verify(playerService, Mockito.times(1)).readById(player.getId());
    }

    @Test
    public void testAddPlayerTeamDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getPlayers().isEmpty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () -> service.addPlayer(team.getId(), player.getId()));
        Assertions.assertEquals("Team Not Found", e.getMessage());
        Assertions.assertTrue(team.getPlayers().isEmpty());

        Mockito.verify(playerService, Mockito.never()).readById(player.getId());
    }

    @Test
    public void testAddMatch() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, null);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(matchService.readById(match.getId())).thenReturn(Optional.of(match));

        Assertions.assertTrue(team.getMatches().isEmpty());
        service.addMatch(team.getId(), match.getId());
        Assertions.assertFalse(team.getMatches().isEmpty());
        Assertions.assertEquals(1, team.getMatches().size());
        Assertions.assertTrue(team.getMatches().contains(match));
        Mockito.verify(matchService, Mockito.times(1)).readById(match.getId());
    }

    @Test
    public void testAddMatchMatchDoesNotExist() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, team);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(matchService.readById(match.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getMatches().isEmpty());
        var e =Assertions.assertThrows(NoSuchElementException.class,() -> service.addMatch(team.getId(), match.getId()));
        Assertions.assertEquals("Match Not Found", e.getMessage());
        Assertions.assertTrue(team.getMatches().isEmpty());

        Mockito.verify(matchService, Mockito.times(1)).readById(match.getId());
    }

    @Test
    public void testAddMatchTeamDoesNotExist() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, team);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.empty());

        Assertions.assertTrue(team.getMatches().isEmpty());
        var e = Assertions.assertThrows(NoSuchElementException.class,() -> service.addMatch(team.getId(), match.getId()));
        Assertions.assertEquals("Team Not Found", e.getMessage());
        Assertions.assertTrue(team.getMatches().isEmpty());

        Mockito.verify(matchService, Mockito.never()).readById(match.getId());
    }
    
    @Test
    public void testRemovePlayer() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        team.addPlayer(player);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.of(player));

        Assertions.assertTrue(team.getPlayers().contains(player));
        service.removePlayer(team.getId(), player.getId());
        Assertions.assertFalse(team.getPlayers().contains(player));
        Assertions.assertTrue(team.getPlayers().isEmpty());
    }

    @Test
    public void testRemovePlayerPlayerDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        team.addPlayer(player);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(playerService.readById(player.getId())).thenReturn(Optional.empty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () -> service.removePlayer(team.getId(), player.getId()));
        Assertions.assertEquals("Player Not Found", e.getMessage());

    }

    @Test
    public void testRemovePlayerTeamDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        team.addPlayer(player);

        Mockito.when(service.readById(team.getId())).thenReturn(Optional.empty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () -> service.removePlayer(team.getId(), player.getId()));
        Assertions.assertEquals("Team Not Found", e.getMessage());
    }

    @Test
    public void testRemoveMatch() {
//        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
//        team.removeMatch(matchService.readById(matchId)
//                .orElseThrow(() -> new NoSuchElementException("Match Not Found")));
//        matchService.deleteById(matchId);
//        update(team);
        // TODO
    }

    @Test
    public void testRemoveMatchMatchDoesNotExist() {
        // TODO
    }

    @Test
    public void testRemoveMatchTeamDoesNotExist() {
        // TODO
    }

    @Test
    public void testDelete() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(service.readById(team.getId())).thenReturn(Optional.of(team));
        service.deleteTeamById(team.getId());
        Mockito.verify(repository, Mockito.times(1)).deleteById(team.getId());
    }

    @Test
    public void testDeleteTeamDoesNotExist() {
        Long id = 1L;
        Mockito.when(service.readById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.deleteTeamById(id));
        Mockito.verify(repository, Mockito.never()).deleteById(id);
    }
}

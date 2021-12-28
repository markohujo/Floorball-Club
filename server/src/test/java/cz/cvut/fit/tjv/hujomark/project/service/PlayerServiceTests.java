package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTests {

    @InjectMocks
    PlayerService service;

    @Mock
    TeamService teamService;

    @Mock
    PlayerJpaRepository repository;

    @Mock
    MatchService matchService;

    @Test
    public void testFindTeams() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Player player = new Player(1L, "Marko", "Hujo", null, null, Set.of(team));

        Mockito.when(repository.findTeams(player.getId())).thenReturn(List.of(team));

        Collection<Team> teamsResult = service.findTeams(player.getId());

        assertEquals(1, teamsResult.size());
        assertEquals(team, teamsResult.toArray()[0]);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findTeams(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue());
    }

    @Test
    public void testFindMatches() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, team);
        Player player = new Player(1L, "Marko", "Hujo", null, null, Set.of(team));

        Mockito.when(repository.findMatches(player.getId())).thenReturn(List.of(match));

        Collection<Match> teamsResult = service.findMatches(player.getId());

        assertEquals(1, teamsResult.size());
        assertEquals(match, teamsResult.toArray()[0]);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findMatches(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue());
    }

    @Test
    public void testUpdateEmail() {
        Player player = new Player(1L, null, null, "hujomark@fit.cvut.cz", null, new HashSet<>());
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));
        assertEquals("hujomark@fit.cvut.cz", player.getEmail());
        service.updateEmail(player.getId(), "hujomark@cvut.cz");
        assertEquals("hujomark@cvut.cz", player.getEmail());
    }

    @Test
    public void testUpdateEmailPlayerDoesNotExist() {
        Player player = new Player(1L, null, null, null, null, new HashSet<>());
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateEmail(player.getId(), "hujomark@cvut.cz"));
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(repository, Mockito.never()).save(playerArgumentCaptor.capture());
    }

    @Test
    public void testAddToTeam() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));
        Assertions.assertFalse(team.getPlayers().contains(player));
        service.addToTeam(player.getId(), team.getId());
        Assertions.assertTrue(team.getPlayers().contains(player));
    }

    @Test
    public void testAddToTeamTeamDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.addToTeam(player.getId(), team.getId()));
    }

    @Test
    public void testAddToTeamPlayerDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.addToTeam(player.getId(), team.getId()));
    }

    @Test
    public void testRemoveFromTeam() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        team.addPlayer(player);

        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));

        Assertions.assertTrue(team.getPlayers().contains(player));
        Assertions.assertFalse(team.getPlayers().isEmpty());

        service.removeFromTeam(player.getId(), team.getId());

        Assertions.assertFalse(team.getPlayers().contains(player));
        Assertions.assertTrue(team.getPlayers().isEmpty());
    }

    @Test
    public void testRemoveFromTeamTeamDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.removeFromTeam(player.getId(), team.getId()));
    }

    @Test
    public void testRemoveFromTeamPlayerDoesNotExist() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        team.addPlayer(player);
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.of(team));
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.removeFromTeam(player.getId(), team.getId()));
    }

    @Test
    public void testDelete() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));
        service.deletePlayerById(player.getId());
        Mockito.verify(repository, Mockito.times(1)).deleteById(player.getId());
    }

    @Test
    public void testDeletePlayerDoesNotExist() {
        Mockito.when(service.readById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.deletePlayerById(1L));
    }

    @Test
    public void testCreateMatchForEachTeam() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Team team1 = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Team team2 = new Team(101L, "Team B", new HashSet<>(), new HashSet<>());
        player.getTeams().add(team1);
        player.getTeams().add(team2);
        Assertions.assertFalse(player.getTeams().isEmpty());
        Assertions.assertEquals(2, player.getTeams().size());

        Assertions.assertTrue(team1.getMatches().isEmpty());
        Assertions.assertTrue(team2.getMatches().isEmpty());

        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));

        service.createMatchForEachTeam(player.getId());
        Assertions.assertFalse(team1.getMatches().isEmpty());
        Assertions.assertFalse(team2.getMatches().isEmpty());
        Assertions.assertEquals(1, team1.getMatches().size());
        Assertions.assertEquals(1, team2.getMatches().size());
    }
}

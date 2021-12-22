package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    TeamService teamService;

    @Mock
    PlayerJpaRepository repository;

    @Mock
    TeamJpaRepository teamRepository;

    @Test
    public void testCreate() {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        service.create(player);

        Mockito.verify(repository, Mockito.times(1)).save(any());
        Mockito.verify(repository, Mockito.times(1)).save(any(Player.class));
        Mockito.verify(repository, Mockito.times(1)).save(player);
    }

    @Test
    public void testReadAll() {
        Player player1 = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());
        Player player2 = new Player(2L, "Someone", "Else", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());
        List<Player> players = List.of(player1, player2);

        Mockito.when(repository.findAll()).thenReturn(players);

        List<Player> playersResult = service.readAll();
        assertEquals(playersResult.size(), 2);
        assertEquals(playersResult.toArray()[0], player1);
        assertEquals(playersResult.toArray()[1], player2);
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void testReadOne() {
        Player player = new Player(100L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Mockito.when(repository.findById(100L)).thenReturn(Optional.of(player));

        Optional<Player> playerResult = service.readById(100L);
        assertNotNull(playerResult);
        assertEquals(playerResult.get(), player);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findById(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), 100);
    }

    @Test
    public void testFindTeams() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), Set.of(team));

        Mockito.when(repository.findTeams(player.getId())).thenReturn(player.getTeams());

        Collection<Team> teamsResult = service.findTeams(player.getId());
        assertEquals(teamsResult.size(), 1);
        assertEquals(teamsResult.toArray()[0], team);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findTeams(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), 1);
    }

    @Test
    public void testFindMatches() {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Match match = new Match(100L, LocalDateTime.of(
                LocalDate.of(2022, Month.JANUARY, 15),
                LocalTime.of(19, 30)), team);

        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), Set.of(team));

        Mockito.when(repository.findMatches(player.getId())).thenReturn(List.of(match));

        Collection<Match> teamsResult = service.findMatches(player.getId());
        assertEquals(teamsResult.size(), 1);
        assertEquals(teamsResult.toArray()[0], match);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findMatches(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), 1);
    }

    @Test
    public void testUpdateEmail() {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Mockito.when(service.readById(player.getId())).thenReturn(Optional.of(player));
        Mockito.when(repository.existsById(player.getId())).thenReturn(true);

        service.updateEmail(player.getId(), "hujomark@cvut.cz");

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(repository, Mockito.times(1)).save(playerArgumentCaptor.capture());
        assertEquals(playerArgumentCaptor.getValue(), player);
        assertEquals(playerArgumentCaptor.getValue().getEmail(), "hujomark@cvut.cz");
    }
}

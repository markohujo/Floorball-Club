package cz.cvut.fit.tjv.hujomark.project.controller;

import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerController;
import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTests {

    @MockBean
    PlayerService playerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateNew() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Mockito.when(playerService.readById(not(eq(1L)))).thenReturn(Optional.empty());
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        String json = "{\"id\":1," +
                      "\"name\":\"Marko\"," +
                      "\"surname\":\"Hujo\"," +
                      "\"email\":\"hujomark@fit.cvut.cz\"," +
                      "\"dateOfBirth\":\"20.8.2001\"}";

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.email", Matchers.is("hujomark@fit.cvut.cz")));

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(playerService, Mockito.times(1)).create(playerArgumentCaptor.capture());
        Player playerProvided = playerArgumentCaptor.getValue();
        assertEquals(playerProvided.getId(), 1);
        assertEquals(playerProvided.getName(), "Marko");
        assertEquals(playerProvided.getEmail(), "hujomark@fit.cvut.cz");
    }

    @Test
    public void testGetAll() throws Exception {
        Player player1 = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());
        Player player2 = new Player(2L, "Someone", "Else", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.SEPTEMBER, 20), new HashSet<>());
        Player player3 = new Player(3L, "Other", "Guy", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.OCTOBER, 20), new HashSet<>());

        List<Player> players = List.of(player1, player2, player3);

        Mockito.when(playerService.readAll()).thenReturn(players);

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[2].id", Matchers.is(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Marko")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Someone")))
                .andExpect(jsonPath("$[2].name", Matchers.is("Other")))
                .andExpect(jsonPath("$[0].email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$[1].email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$[2].email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$[0].dateOfBirth", Matchers.is("20.8.2001")))
                .andExpect(jsonPath("$[1].dateOfBirth", Matchers.is("20.9.2001")))
                .andExpect(jsonPath("$[2].dateOfBirth", Matchers.is("20.10.2001")));
    }

    @Test
    public void testGetOne() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());
        
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.surname", Matchers.is("Hujo")))
                .andExpect(jsonPath("$.email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("20.8.2001")))
                .andExpect(jsonPath("$.teams", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.teams", Matchers.is(Matchers.empty())));

        Team team1 = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Team team2 = new Team(101L, "Team B", new HashSet<>(), new HashSet<>());
        Team team3 = new Team(102L, "Team C", new HashSet<>(), new HashSet<>());

        player.getTeams().add(team1);

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.surname", Matchers.is("Hujo")))
                .andExpect(jsonPath("$.email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("20.8.2001")))
                .andExpect(jsonPath("$.teams", Matchers.hasSize(1)));

        player.getTeams().add(team2);
        player.getTeams().add(team3);

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.surname", Matchers.is("Hujo")))
                .andExpect(jsonPath("$.email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("20.8.2001")))
                .andExpect(jsonPath("$.teams", Matchers.hasSize(3)));
    }

    @Test
    public void testGetTeams() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Team team1 = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Team team2 = new Team(101L, "Team B", new HashSet<>(), new HashSet<>());
        Team team3 = new Team(102L, "Team C", new HashSet<>(), new HashSet<>());
        Collection<Team> teams = Set.of(team1, team2, team3);
        teams.forEach(team -> player.getTeams().add(team));

        Mockito.when(playerService.findTeams(1L)).thenReturn(teams);

        mockMvc.perform(get("/players/1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.anyOf(
                        Matchers.is(100),
                        Matchers.is(101),
                        Matchers.is(102))))
                .andExpect(jsonPath("$[1].id", Matchers.anyOf(
                        Matchers.is(100),
                        Matchers.is(101),
                        Matchers.is(102))))
                .andExpect(jsonPath("$[2].id", Matchers.anyOf(
                        Matchers.is(100),
                        Matchers.is(101),
                        Matchers.is(102))));
    }

    @Test
    public void testGetMatches() throws Exception {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());

        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), Set.of(team));

        Match match1 = new Match(100L, LocalDateTime.of(
                LocalDate.of(2022, Month.JANUARY, 15),
                LocalTime.of(19, 30)), team);
        Match match2 = new Match(101L, LocalDateTime.of(
                LocalDate.of(2022, Month.JANUARY, 20),
                LocalTime.of(17, 0)), team);
        Set<Match> matches = Set.of(match1, match2);

        Mockito.when(playerService.findMatches(1L)).thenReturn(matches);

        mockMvc.perform(get("/players/1/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.anyOf(
                        Matchers.is(100),
                        Matchers.is(101))))
                .andExpect(jsonPath("$[1].id", Matchers.anyOf(
                        Matchers.is(100),
                        Matchers.is(101))));
    }

    @Test
    public void testUpdateEmail() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(put("/players/1").param("email", "hujomark@fit.cvut.cz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(player.getId().intValue())))
                .andExpect(jsonPath("$.email", Matchers.is("hujomark@fit.cvut.cz")));

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(playerService, Mockito.times(1))
                .updateEmail(longArgumentCaptor.capture(), stringArgumentCaptor.capture());
        Long idProvided = longArgumentCaptor.getValue();
        String emailProvided = stringArgumentCaptor.getValue();
        assertEquals(idProvided, 1);
        assertEquals(emailProvided, "hujomark@fit.cvut.cz");
    }

    @Test
    public void testAddToTeam() throws Exception {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), Set.of(team));

        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(put("/players/1/teams/add").param("teamId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.teams[0]", Matchers.is(team.getId().intValue())));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(playerService, Mockito.times(1))
                .addToTeam(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long teamIdProvided = longArgumentCaptor2.getValue();
        assertEquals(idProvided, 1);
        assertEquals(teamIdProvided, 100);
    }

    @Test
    public void testRemoveFromTeam() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz",
                LocalDate.of(2001, Month.AUGUST, 20), new HashSet<>());

        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(put("/players/1/teams/remove").param("teamId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", Matchers.hasSize(0)));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(playerService, Mockito.times(1))
                .removeFromTeam(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long teamIdProvided = longArgumentCaptor2.getValue();
        assertEquals(idProvided, 1);
        assertEquals(teamIdProvided, 100);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/players/1"))
                .andExpect(status().isOk());
        Mockito.verify(playerService, Mockito.times(1)).deletePlayerById(1L);
    }

}

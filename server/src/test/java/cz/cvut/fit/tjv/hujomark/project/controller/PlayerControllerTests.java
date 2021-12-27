package cz.cvut.fit.tjv.hujomark.project.controller;

import cz.cvut.fit.tjv.hujomark.project.api.controller.PlayerController;
import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        Player player = new Player(1L, "Marko", "Hujo", null, null, Collections.emptySet());
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));
        Mockito.when(playerService.create(player)).thenReturn(player);

        String json = "{\"id\":1," +
                      "\"name\":\"Marko\"," +
                      "\"surname\":\"Hujo\"," +
                      "\"email\":\"\"," +
                      "\"dateOfBirth\":\"\"}";

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.surname", Matchers.is("Hujo")))
                .andExpect(jsonPath("$.teams", Matchers.hasSize(0)));
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(playerService.readAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Player player1 = new Player(1L, "Marko", "Hujo", "hujomark@fit.cvut.cz", null, Collections.emptySet());
        Player player2 = new Player(2L, null, null, null, null, Collections.emptySet());
        Player player3 = new Player(3L, null, null, null, null, Collections.emptySet());
        List<Player> players = List.of(player1, player2, player3);
        Mockito.when(playerService.readAll()).thenReturn(players);

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[2].id", Matchers.is(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Marko")))
                .andExpect(jsonPath("$[0].email", Matchers.is("hujomark@fit.cvut.cz")))
                .andExpect(jsonPath("$[0].dateOfBirth", Matchers.nullValue()))
                .andExpect(jsonPath("$[0].teams", Matchers.hasSize(0)))
                .andExpect(jsonPath("$[0].teams", Matchers.empty()))
                .andExpect(jsonPath("$[1].name", Matchers.nullValue()))
                .andExpect(jsonPath("$[2].email", Matchers.nullValue()));
    }

    @Test
    public void testGetOne() throws Exception {
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/players/1"))
                .andExpect(status().isNotFound());

        Player player = new Player(1L, "Marko", "Hujo", null, null, Collections.emptySet());
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Marko")))
                .andExpect(jsonPath("$.surname", Matchers.is("Hujo")))
                .andExpect(jsonPath("$.teams", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.teams", Matchers.is(Matchers.empty())));
    }

    @Test
    public void testGetTeams() throws Exception {
        Player player = new Player(1L, "Marko", "Hujo", null, null,
                new HashSet<>());

        Team team1 = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Team team2 = new Team(101L, "Team B", new HashSet<>(), new HashSet<>());
        Team team3 = new Team(102L, "Team C", new HashSet<>(), new HashSet<>());
        List<Team> teams = List.of(team1, team2, team3);
        teams.forEach(team -> player.getTeams().add(team));

        Mockito.when(playerService.findTeams(1L)).thenReturn(teams);
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(100)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Team A")))
                .andExpect(jsonPath("$[1].id", Matchers.is(101)))
                .andExpect(jsonPath("$[1].name", Matchers.is("Team B")))
                .andExpect(jsonPath("$[2].id", Matchers.is(102)))
                .andExpect(jsonPath("$[2].name", Matchers.is("Team C")));
    }

    @Test
    public void testGetMatches() throws Exception {
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Player player = new Player(1L, "Marko", "Hujo", null, null, Set.of(team));

        Match match1 = new Match(1000L, null, team);
        Match match2 = new Match(1001L, null, team);
        Match match3 = new Match(1002L, null, team);
        List<Match> matches = List.of(match1, match2, match3);

        Mockito.when(playerService.findMatches(1L)).thenReturn(matches);
        Mockito.when(playerService.readById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/1/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1000)))
                .andExpect(jsonPath("$[1].id", Matchers.is(1001)))
                .andExpect(jsonPath("$[2].id", Matchers.is(1002)));
    }

    // OK

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
                .andExpect(status().isNoContent());
        Mockito.verify(playerService, Mockito.times(1)).deletePlayerById(1L);
    }

}

package cz.cvut.fit.tjv.hujomark.project.controller;

import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamController;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTests {

    @MockBean
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateNew() throws Exception {
        Team team = new Team(1L, "Team A", Collections.emptySet(), Collections.emptySet());
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));
        Mockito.when(teamService.create(team)).thenReturn(team);

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Team A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Team A")))
                .andExpect(jsonPath("$.players", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.matches", Matchers.hasSize(0)));
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(teamService.readAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Team team1 = new Team(1L, "Team A", Collections.emptySet(), Collections.emptySet());
        Team team2 = new Team(2L, null, Collections.emptySet(), Collections.emptySet());
        Team team3 = new Team(3L, null, Collections.emptySet(), Collections.emptySet());
        List<Team> teams = List.of(team1, team2, team3);
        Mockito.when(teamService.readAll()).thenReturn(teams);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[2].id", Matchers.is(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Team A")));
    }

    @Test
    public void testGetOne() throws Exception {
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/teams/1")).andExpect(status().isNotFound());

        Team team = new Team(1L, "Team A", Collections.emptySet(), Collections.emptySet());
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(get("/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Team A")))
                .andExpect(jsonPath("$.players", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.players", Matchers.is(Matchers.empty())))
                .andExpect(jsonPath("$.matches", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.matches", Matchers.is(Matchers.empty())));
    }

    @Test
    public void testGetPlayers() throws Exception {
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/teams/1/players")).andExpect(status().isNotFound());

        Team team = new Team(1L, "Team A", new HashSet<>(), new HashSet<>());
        Player player1 = new Player(100L, null, null, null, null, new HashSet<>());
        Player player2 = new Player(101L, null, null, null, null, new HashSet<>());
        List<Player> players = List.of(player1, player2);
        players.forEach(team::addPlayer);

        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(get("/teams/1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(100)))
                .andExpect(jsonPath("$[1].id", Matchers.is(101)));
    }

    @Test
    public void testGetMatches() throws Exception {
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/teams/1/matches")).andExpect(status().isNotFound());

        Team team = new Team(1L, "Team A", new HashSet<>(), new HashSet<>());
        Match match1 = new Match(100L, null, team);
        Match match2 = new Match(101L, null, team);
        List<Match> matches = List.of(match1, match2);
        matches.forEach(team::addMatch);

        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(get("/teams/1/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(100)))
                .andExpect(jsonPath("$[0].teamId", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(101)))
                .andExpect(jsonPath("$[1].teamId", Matchers.is(1)));
    }

    @Test
    public void testAddPlayer() throws Exception {
        Player player = new Player(100L, null, null, null, null, new HashSet<>());
        Team team = new Team(1L, "Team A", Set.of(player), new HashSet<>());
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(put("/teams/1/players/add").param("player", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.players[0]", Matchers.is(player.getId().intValue())));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(teamService, Mockito.times(1))
                .addPlayer(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long playerIdProvided = longArgumentCaptor2.getValue();
        assertEquals(1, idProvided);
        assertEquals(100, playerIdProvided);

        Mockito.when(teamService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/teams/1/players/add").param("player", "100"))
                .andExpect(status().isNotFound());
        Mockito.verify(teamService, Mockito.times(2))
                .addPlayer(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
    }

    @Test
    public void testRemovePlayer() throws Exception {
        Team team = new Team(1L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(put("/teams/1/players/remove").param("player", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(team.getId().intValue())));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(teamService, Mockito.times(1))
                .removePlayer(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long playerIdProvided = longArgumentCaptor2.getValue();
        assertEquals(1, idProvided);
        assertEquals(100, playerIdProvided);
    }

    @Test
    public void testAddMatch() throws Exception {
        Team team = new Team(1L, "Team A", new HashSet<>(), new HashSet<>());
        Match match = new Match(100L, null, team);
        team.addMatch(match);
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(put("/teams/1/matches/add").param("match", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.matches[0]", Matchers.is(match.getId().intValue())));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(teamService, Mockito.times(1))
                .addMatch(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long matchIdProvided = longArgumentCaptor2.getValue();
        assertEquals(1, idProvided);
        assertEquals(100, matchIdProvided);

        Mockito.when(teamService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/teams/1/matches/add").param("match", "100"))
                .andExpect(status().isNotFound());
        Mockito.verify(teamService, Mockito.times(2))
                .addMatch(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
    }

    @Test
    public void testRemoveMatch() throws Exception {
        Team team = new Team(1L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(teamService.readById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(put("/teams/1/matches/remove").param("match", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(team.getId().intValue())));

        ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(teamService, Mockito.times(1))
                .removeMatch(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
        Long idProvided = longArgumentCaptor1.getValue();
        Long matchIdProvided = longArgumentCaptor2.getValue();
        assertEquals(1, idProvided);
        assertEquals(100, matchIdProvided);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/teams/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(teamService, Mockito.times(1)).deleteTeamById(1L);
    }

    @Test
    public void testGetMatchesExceptTeam() throws Exception {
        Team team1 = new Team(10L, "Team A", Collections.emptySet(), Collections.emptySet());
        Team team2 = new Team(11L, "Team B", Collections.emptySet(), Collections.emptySet());
        Team team3 = new Team(12L, "Team C", Collections.emptySet(), Collections.emptySet());
        Match match1 = new Match(1L, null, team1);
        Match match2 = new Match(2L, null, team1);
        Match match3 = new Match(3L, null, team2);
        Match match4 = new Match(4L, null, team2);
        Match match5 = new Match(5L, null, team3);
        List<Match> matchesExceptTeam1 = List.of(match3, match4, match5);
        Mockito.when(teamService.availableMatches(10L)).thenReturn(matchesExceptTeam1);

        mockMvc.perform(get("/teams/10/matches/available").param("teamId", team1.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(3)))
                .andExpect(jsonPath("$[0].teamId", Matchers.is(11)))
                .andExpect(jsonPath("$[1].id", Matchers.is(4)))
                .andExpect(jsonPath("$[1].teamId", Matchers.is(11)))
                .andExpect(jsonPath("$[2].id", Matchers.is(5)))
                .andExpect(jsonPath("$[2].teamId", Matchers.is(12)));
    }
}

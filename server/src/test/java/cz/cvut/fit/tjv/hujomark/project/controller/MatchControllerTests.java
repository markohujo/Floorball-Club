package cz.cvut.fit.tjv.hujomark.project.controller;

import cz.cvut.fit.tjv.hujomark.project.api.controller.MatchController;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchControllerTests {

    @MockBean
    MatchService matchService;

    @MockBean
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateNew() throws Exception {
        Team team = new Team(100L, null, Collections.emptySet(), Collections.emptySet());
        Match match = new Match(1L, null, team);
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.of(match));
        Mockito.when(teamService.readById(100L)).thenReturn(Optional.of(team));
        Mockito.when(matchService.create(match)).thenReturn(match);

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"dateTime\":\"\",\"teamId\":50}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"dateTime\":\"\",\"teamId\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.teamId", Matchers.is(100)));
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(matchService.readAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Team team = new Team(1L, "TeamA", Collections.emptySet(), Collections.emptySet());
        Match match1 = new Match(1L, null, team);
        Match match2 = new Match(2L, null, team);
        Match match3 = new Match(3L, null, team);
        List<Match> matches = List.of(match1, match2, match3);
        Mockito.when(matchService.readAll()).thenReturn(matches);

        mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[2].id", Matchers.is(3)))
                .andExpect(jsonPath("$[0].teamId", Matchers.is(1)))
                .andExpect(jsonPath("$[1].teamId", Matchers.is(1)))
                .andExpect(jsonPath("$[2].teamId", Matchers.is(1)));
    }

    @Test
    public void testGetOne() throws Exception {
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/matches/1")).andExpect(status().isNotFound());

        Team team = new Team(10L, "Team A", Collections.emptySet(), Collections.emptySet());
        Match match1 = new Match(1L, null, team);
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.of(match1));

        mockMvc.perform(get("/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.teamId", Matchers.is(10)));
    }

    @Test
    public void testGetTeam() throws Exception {
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/matches/1/team")).andExpect(status().isNotFound());

        Team team1 = new Team(10L, "Team A", Collections.emptySet(), Collections.emptySet());
        Team team2 = new Team(11L, "Team B", Collections.emptySet(), Collections.emptySet());
        Match match = new Match(1L, null, team1);
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.of(match));

        mockMvc.perform(get("/matches/1/team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(10)))
                .andExpect(jsonPath("$.name", Matchers.is("Team A")));

        match.setTeam(team2);

        mockMvc.perform(get("/matches/1/team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(11)))
                .andExpect(jsonPath("$.name", Matchers.is("Team B")));
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
        Mockito.when(matchService.exceptTeamWithId(10L)).thenReturn(matchesExceptTeam1);

        mockMvc.perform(get("/matches/except").param("teamId", team1.getId().toString()))
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

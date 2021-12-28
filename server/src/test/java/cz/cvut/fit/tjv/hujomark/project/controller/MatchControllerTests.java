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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testUpdate() throws Exception {
        Team team = new Team(10L, "Team A", Collections.emptySet(), Collections.emptySet());
        Match match = new Match(1L, LocalDateTime.of(2022,1, 20, 17, 30), team);
        Mockito.when(matchService.readById(1L)).thenReturn(Optional.of(match));

        mockMvc.perform(put("/matches/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"dateTime\":\"20.1.2022 17:30\",\"teamId\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.dateTime", Matchers.is("20.1.2022 17:30")))
                .andExpect(jsonPath("$.teamId", Matchers.is(10)));

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(matchService, Mockito.times(1))
                .updateTeam(longArgumentCaptor.capture(), longArgumentCaptor2.capture());

        Mockito.verify(matchService, Mockito.times(1))
                .updateDateTime(longArgumentCaptor.capture(), stringArgumentCaptor.capture());

        Long idProvided = longArgumentCaptor.getValue();
        Long teamIdProvided = longArgumentCaptor2.getValue();
        String dateTimeProvided = stringArgumentCaptor.getValue();
        assertEquals(idProvided, 1);
        assertEquals(teamIdProvided, 10);
        assertEquals(dateTimeProvided, "2022-01-20T17:30");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/matches/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(matchService, Mockito.times(1)).deleteById(1L);
    }
}

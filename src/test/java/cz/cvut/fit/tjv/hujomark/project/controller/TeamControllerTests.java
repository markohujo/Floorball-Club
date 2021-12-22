package cz.cvut.fit.tjv.hujomark.project.controller;

import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamController;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

        Team team1 = new Team(1L, "TeamA", Collections.emptySet(), Collections.emptySet());
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
        mockMvc.perform(get("/teams/1"))
                .andExpect(status().isNotFound());

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

}

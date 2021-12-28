package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.AbstractCrudService;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTests {

    @InjectMocks
    MatchService service;

    @Mock
    MatchJpaRepository repository;

    @Mock
    private TeamService teamService;

    @Test
    public void testUpdateDateTime() {
        Match match = new Match(10L, null, null);
        LocalDateTime dateTime = LocalDateTime.of(2021, 12, 31, 17, 30);
        Mockito.when(service.readById(match.getId())).thenReturn(Optional.of(match));
        Assertions.assertNotEquals(dateTime, match.getDateTime());
        service.updateDateTime(match.getId(), dateTime.toString());
        Assertions.assertEquals(dateTime, match.getDateTime());
    }

    @Test
    public void testUpdateDateTimeMatchDoesNotExist() {
        Match match = new Match(10L, null, null);
        LocalDateTime dateTime = LocalDateTime.of(2021, 12, 31, 17, 30);
        Mockito.when(service.readById(match.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateDateTime(match.getId(), dateTime.toString()));
    }

    @Test
    public void testUpdateTeam() {
        Team oldTeam = new Team(1L, "Team X", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, oldTeam);
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(service.readById(match.getId())).thenReturn(Optional.of(match));
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.of(team));
        Assertions.assertNotEquals(team, match.getTeam());
        service.updateTeam(match.getId(), team.getId());
        Assertions.assertEquals(team, match.getTeam());
    }

    @Test
    public void testUpdateTeamMatchDoesNotExist() {
        Match match = new Match(10L, null, null);
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(service.readById(match.getId())).thenReturn(Optional.empty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () ->service.updateTeam(match.getId(), team.getId()));
        Assertions.assertEquals("Match Not Found", e.getMessage());
    }

    @Test
    public void testUpdateTeamTeamDoesNotExist() {
        Team oldTeam = new Team(1L, "Team X", new HashSet<>(), new HashSet<>());
        Match match = new Match(10L, null, oldTeam);
        Team team = new Team(100L, "Team A", new HashSet<>(), new HashSet<>());
        Mockito.when(service.readById(match.getId())).thenReturn(Optional.of(match));
        Mockito.when(teamService.readById(team.getId())).thenReturn(Optional.empty());
        var e = Assertions.assertThrows(NoSuchElementException.class, () ->service.updateTeam(match.getId(), team.getId()));
        Assertions.assertEquals("Team Not Found", e.getMessage());
    }
}

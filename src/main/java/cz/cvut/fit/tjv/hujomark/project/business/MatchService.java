package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Component
public class MatchService extends AbstractCrudService<Match, Long, MatchJpaRepository> {
    private final TeamService teamService;

    protected MatchService(MatchJpaRepository repository, TeamService teamService) {
        super(repository);
        this.teamService = teamService;
    }

    @Override
    protected boolean exists(Match entity) {
        return repository.existsById(entity.getId());
    }

    @Transactional
    public void updateDateTime(Long id, String dateTimeStr) {
        Match match = readById(id).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        match.setDateTime(dateTime);
        update(match);
    }

    @Transactional
    public void updateTeam(Long id, Long teamId) {
        Match match = readById(id).orElseThrow();
        if (!match.getTeam().getId().equals(teamId)) {
            match.getTeam().removeMatch(match);
            match.setTeam(teamService.readById(teamId).orElseThrow());
            teamService.readById(teamId).orElseThrow().addMatch(match);
            update(match);
        }
    }
}

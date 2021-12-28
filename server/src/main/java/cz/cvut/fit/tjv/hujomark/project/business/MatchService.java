package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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

    /**
     * @throws NoSuchElementException if no match with the given id is found
     */
    @Transactional
    public void updateDateTime(Long id, String dateTimeStr) {
        Match match = readById(id).orElseThrow(() -> new NoSuchElementException("Match Not Found"));
        StringBuilder dateTimeStrNew = new StringBuilder();
        for (int i = 0; i < dateTimeStr.length(); i++) {
            if (dateTimeStr.charAt(i) == 'T')
                dateTimeStrNew.append(' ');
            else
                dateTimeStrNew.append(dateTimeStr.charAt(i));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStrNew, formatter);
        match.setDateTime(dateTime);
        update(match);
    }

    /**
     * @throws NoSuchElementException if no match with the given id is found
     * @throws NoSuchElementException if no team with the given teamId is found
     */
    @Transactional
    public void updateTeam(Long id, Long teamId) {
        Match match = readById(id).orElseThrow(() -> new NoSuchElementException("Match Not Found"));
        if (!match.getTeam().getId().equals(teamId)) {
            match.getTeam().removeMatch(match);
            match.setTeam(teamService.readById(teamId).orElseThrow(() -> new NoSuchElementException("Team Not Found")));
            teamService.readById(teamId).orElseThrow().addMatch(match);
            update(match);
        }
    }
}

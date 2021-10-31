package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MatchService extends AbstractCrudService<Match, Long, MatchJpaRepository> {
    protected MatchService(MatchJpaRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Match entity) {
        return repository.existsById(entity.getId());
    }

    @Transactional
    public void updateDateTime(Long id, String dateTimeStr) {
        Match match = readById(id).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd;HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        match.setDateTime(dateTime);
        update(match);
    }
}

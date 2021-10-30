package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
    public void updateDateTime(Long id, LocalDateTime dateTime) {
        Match match = readById(id).orElseThrow();
        match.setDateTime(dateTime);
        update(match);
    }
}

package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchService extends AbstractCrudService<Match, Long, MatchJpaRepository> {
    protected MatchService(MatchJpaRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Match entity) {
        return repository.existsById(entity.getId());
    }
}

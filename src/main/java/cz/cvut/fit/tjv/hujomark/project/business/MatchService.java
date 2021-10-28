package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public class MatchService extends AbstractCrudService<Match, Long> {
    public MatchService(MatchJpaRepository matchJpaRepository) {
        super(matchJpaRepository);
    }
}

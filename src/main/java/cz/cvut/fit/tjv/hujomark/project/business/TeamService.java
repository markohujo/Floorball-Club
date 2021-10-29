package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamService extends AbstractCrudService<Team, Long, TeamJpaRepository> {
    protected TeamService(TeamJpaRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Team entity) {
        return repository.existsById(entity.getId());
    }
}

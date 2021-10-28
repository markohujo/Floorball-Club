package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public class TeamService extends AbstractCrudService<Team, Long> {
    public TeamService(TeamJpaRepository teamJpaRepository) {
        super(teamJpaRepository);
    }
}

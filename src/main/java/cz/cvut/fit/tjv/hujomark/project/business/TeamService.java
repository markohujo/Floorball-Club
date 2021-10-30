package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
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

    public void addPlayer(Long id, Player player) {
        Team team = readById(id).orElseThrow();
        team.addPlayer(player);
        update(team);
    }

    public void addMatch(Long id, Match match) {
        Team team = readById(id).orElseThrow();
        team.addMatch(match);
        update(team);
    }

    public void removePlayer(Long id, Player player) {
        Team team = readById(id).orElseThrow();
        team.removePlayer(player);
        update(team);
    }
}

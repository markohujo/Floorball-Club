package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TeamService extends AbstractCrudService<Team, Long, TeamJpaRepository> {
    private final MatchService matchService;
    private final PlayerService playerService;

    protected TeamService(TeamJpaRepository repository, MatchService matchService, @Lazy PlayerService playerService) {
        super(repository);
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @Override
    protected boolean exists(Team entity) {
        return repository.existsById(entity.getId());
    }

    public void addPlayer(Long id, Long player) {
        Team team = readById(id).orElseThrow();
        team.addPlayer(playerService.readById(player).orElseThrow());
        update(team);
    }

    public void addMatch(Long id, Long match) {
        Team team = readById(id).orElseThrow();
        team.addMatch(matchService.readById(match).orElseThrow());
        update(team);
    }

    public void removePlayer(Long id, Long player) {
        Team team = readById(id).orElseThrow();
        team.removePlayer(playerService.readById(player).orElseThrow());
        update(team);
    }

    /**
     * Apart from removing match from this team's matches, match itself gets deleted
     */
    public void removeMatch(Long id, Long match) {
        Team team = readById(id).orElseThrow();
        team.removeMatch(matchService.readById(match).orElseThrow());
        // match gets deleted as there is always only one team in relationship with this match
        matchService.deleteById(match);
        update(team);
    }

    public void addOrRemovePlayer(Long id, Long player, boolean remove) {
        if (remove) removePlayer(id, player);
        else addPlayer(id, player);
    }

    public void addOrRemoveMatch(Long id, Long match, boolean remove) {
        if (remove) removeMatch(id, match);
        else addMatch(id, match);
    }
}

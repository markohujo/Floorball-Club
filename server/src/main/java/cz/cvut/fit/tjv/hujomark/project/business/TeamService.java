package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class TeamService extends AbstractCrudService<Team, Long, TeamJpaRepository> {
    private final MatchService matchService;
    private final PlayerService playerService;

    protected TeamService(TeamJpaRepository repository, @Lazy MatchService matchService, @Lazy PlayerService playerService) {
        super(repository);
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @Override
    protected boolean exists(Team entity) {
        return repository.existsById(entity.getId());
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no player with the given playerId is found
     */
    public void addPlayer(Long id, Long playerId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.addPlayer(playerService.readById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player Not Found")));
        update(team);
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no match with the given matchId is found
     */
    public void addMatch(Long id, Long matchId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.addMatch(matchService.readById(matchId)
                .orElseThrow(() -> new NoSuchElementException("Match Not Found")));
        update(team);
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no player with the given playerId is found
     */
    public void removePlayer(Long id, Long playerId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.removePlayer(playerService.readById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player Not Found")));
        update(team);
    }

    /**
     * Apart from removing match from this team's matches, match itself gets deleted
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no match with the given matchId is found
     */
    public void removeMatch(Long id, Long matchId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.removeMatch(matchService.readById(matchId)
                .orElseThrow(() -> new NoSuchElementException("Match Not Found")));
        // match gets deleted as there is always only one team in relationship with this match
        matchService.deleteById(matchId);
        update(team);
    }
}
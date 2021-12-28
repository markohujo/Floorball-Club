package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
public class TeamService extends AbstractCrudService<Team, Long, TeamJpaRepository> {
    private final MatchService matchService;
    private final PlayerService playerService;

    private final MatchJpaRepository matchJpaRepository;

    protected TeamService(TeamJpaRepository repository, @Lazy MatchService matchService, @Lazy PlayerService playerService, MatchJpaRepository matchJpaRepository) {
        super(repository);
        this.matchService = matchService;
        this.playerService = playerService;
        this.matchJpaRepository = matchJpaRepository;
    }

    @Override
    protected boolean exists(Team entity) {
        return repository.existsById(entity.getId());
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no player with the given playerId is found
     */
    @Transactional
    public void addPlayer(Long id, Long playerId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.addPlayer(playerService.readById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player Not Found")));
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no match with the given matchId is found
     */
    @Transactional
    public void addMatch(Long id, Long matchId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        Match match = matchService.readById(matchId).orElseThrow(() -> new NoSuchElementException("Match Not Found"));
        team.addMatch(match);
    }

    /**
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no player with the given playerId is found
     */
    @Transactional
    public void removePlayer(Long id, Long playerId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.removePlayer(playerService.readById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player Not Found")));
    }

    /**
     * Apart from removing match from this team's matches, match itself gets deleted
     * @throws NoSuchElementException if no team with the given id is found
     * @throws NoSuchElementException if no match with the given matchId is found
     */
    @Transactional
    public void removeMatch(Long id, Long matchId) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.removeMatch(matchService.readById(matchId)
                .orElseThrow(() -> new NoSuchElementException("Match Not Found")));
        // match gets deleted as there is always only one team in relationship with this match
        matchService.deleteById(matchId);
    }

    /**
     * Apart from deleting this team, all matches played by this team are deleted
     * @throws NoSuchElementException if no team with the given id is found
     */
    public void deleteTeamById(Long id) {
        Team team = readById(id).orElseThrow(() -> new NoSuchElementException("Team Not Found"));
        team.getMatches().forEach(match -> matchService.deleteById(match.getId()));
        deleteById(id);
    }

    /**
     * @return all available matches for the team with the given id,
     *         in other words matches except those that are played by the team with the given id;
     */
    public Collection<Match> availableMatches(Long id) {
        return matchJpaRepository.findMatchesExcept(id);
    }
}

package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.api.controller.MatchDto;
import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
public class PlayerService extends AbstractCrudService<Player, Long, PlayerJpaRepository> {
    private final TeamService teamService;
    private final MatchService matchService;
    private TeamJpaRepository teamJpaRepository;

    protected PlayerService(PlayerJpaRepository repository, TeamService teamService, MatchService matchService,
                            TeamJpaRepository teamJpaRepository) {
        super(repository);
        this.teamService = teamService;
        this.matchService = matchService;
        this.teamJpaRepository = teamJpaRepository;
    }

    @Override
    protected boolean exists(Player entity) {
        return repository.existsById(entity.getId());
    }

    public Collection<Team> findTeams(Long id) {
        return repository.findTeams(id);
    }

    public Collection<Match> findMatches(Long id) {
        return repository.findMatches(id);
    }

    /**
     * @param id        primary key of player
     * @param email     new email of player
     * @throws NoSuchElementException if no player with the given id is found
     */
    @Transactional
    public void updateEmail(Long id, String email) {
        Player player = readById(id).orElseThrow();
        player.setEmail(email);
    }

    /**
     * @throws NoSuchElementException if no player with the given id is found
     * @throws NoSuchElementException if no team with the given teamId is found
     */
    @Transactional
    public void addToTeam(Long id, Long teamId) {
        teamService.readById(teamId).orElseThrow(() -> new NoSuchElementException("Team Not Found"))
                .addPlayer(readById(id).orElseThrow(() -> new NoSuchElementException("Player Not Found")));
    }

    /**
     * @throws NoSuchElementException if no player with the given id is found
     * @throws NoSuchElementException if no team with the given teamId is found
     */
    @Transactional
    public void removeFromTeam(Long id, Long teamId) {
        teamService.readById(teamId).orElseThrow(() -> new NoSuchElementException("Team Not Found"))
                .removePlayer(readById(id).orElseThrow(() -> new NoSuchElementException("Player Not Found")));
    }

    /**
     * @throws NoSuchElementException if no player with the given id is found
     */
    public void deletePlayerById(Long id) {
        Player player = readById(id).orElseThrow();
        player.getTeams().forEach(team -> team.removePlayer(player));
        deleteById(id);
    }

    /**
     * Creates a new match for each team in which the player with the given id plays
     * @throws NoSuchElementException if no player with the given id is found
     */
    @Transactional
    public void createMatchForEachTeam(Long id) {
        Player player = readById(id).orElseThrow();
        player.getTeams().forEach(team -> {
            Match newMatch = matchService.create(new Match(null, LocalDateTime.now(), team));
            team.addMatch(newMatch);
        });
    }

    public Collection<Team> availableTeams(Long id) {
        return teamJpaRepository.findTeamsExcept(id);
    }
}

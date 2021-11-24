package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamDto;
import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class PlayerService extends AbstractCrudService<Player, Long, PlayerJpaRepository> {
    private final TeamService teamService;

    protected PlayerService(PlayerJpaRepository repository, TeamService teamService) {
        super(repository);
        this.teamService = teamService;
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
        update(player);
    }

    @Transactional
    public void addToTeam(Long id, Long teamId) {
        teamService.readById(teamId).orElseThrow().addPlayer(readById(id).orElseThrow());
    }

    @Transactional
    public void removeFromTeam(Long id, Long teamId) {
        teamService.readById(teamId).orElseThrow().removePlayer(readById(id).orElseThrow());
    }

    public void deletePlayerById(Long id) {
        Player player = readById(id).orElseThrow();
        player.getTeams().forEach(team -> team.removePlayer(player));
        deleteById(id);
    }
}

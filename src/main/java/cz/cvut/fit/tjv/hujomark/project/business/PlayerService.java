package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
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

    /**
     * Iterates over all teams and determines whether each team contains player with the given id
     * @param id primary key of player
     * @throws NoSuchElementException if no player with the given id is found
     * TODO select in db ???
     */
    public Collection<Team> findTeams(Long id) {
        Set<Team> teams = new HashSet<>();
        teamService.readAll().forEach(team -> {
            if (team.getPlayers().contains(readById(id).orElseThrow()))
                teams.add(team);
        });
        return teams;
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
}

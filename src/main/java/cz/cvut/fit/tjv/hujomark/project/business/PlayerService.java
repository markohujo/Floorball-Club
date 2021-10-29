package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Component
public class PlayerService extends AbstractCrudService<Player, Long, PlayerJpaRepository> {
    protected PlayerService(PlayerJpaRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Player entity) {
        return repository.existsById(entity.getId());
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

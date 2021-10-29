package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerService extends AbstractCrudService<Player, Long, PlayerJpaRepository> {
    protected PlayerService(PlayerJpaRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Player entity) {
        return repository.existsById(entity.getId());
    }
}

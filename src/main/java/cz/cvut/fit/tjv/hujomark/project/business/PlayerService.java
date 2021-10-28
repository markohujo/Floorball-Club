package cz.cvut.fit.tjv.hujomark.project.business;

import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public class PlayerService extends AbstractCrudService<Player, Long> {
    public PlayerService(PlayerJpaRepository playerJpaRepository) {
        super(playerJpaRepository);
    }
}

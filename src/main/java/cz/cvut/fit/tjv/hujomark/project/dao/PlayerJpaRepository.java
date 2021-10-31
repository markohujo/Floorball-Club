package cz.cvut.fit.tjv.hujomark.project.dao;

import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlayerJpaRepository extends JpaRepository<Player, Long> {

    @Query(nativeQuery = true, value = "select t.* from player p " +
                                       "join team_player tp on p.id = tp.players_id" +
                                       "join team t on tp.team_id = t.id ")
    Collection<Team> findTeams(Long id);
}

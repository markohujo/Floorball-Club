package cz.cvut.fit.tjv.hujomark.project.dao;

import cz.cvut.fit.tjv.hujomark.project.api.controller.TeamDto;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlayerJpaRepository extends JpaRepository<Player, Long> {


//    @Query(nativeQuery = true, value = "select t.* from team t " +
//        "join player_team pt on t.id = pt.team_id " +
//        "join player p on pt.player_id = p.id " +
//        "where p.id = :id")
    @Query("select t from Team t join t.players p where p.id = :id")
    Collection<Team> findTeams(@Param("id") Long id);

//    @Query(nativeQuery = true, value = "select m.* from match m " +
//        "join team t on m.team_id = t.id " +
//        "join player_team pt on t.id = pt.team_id " +
//        "where pt.player_id = :id")
    @Query("select m from Team t join t.matches m join t.players p where p.id = :id")
    Collection<Match> findMatches(@Param("id") Long id);
}

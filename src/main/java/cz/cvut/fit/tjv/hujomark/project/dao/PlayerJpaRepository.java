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

    @Query("select t from Team t join t.players p where p.id = :id")
    Collection<Team> findTeams(@Param("id") Long id);

    @Query("select m from Team t join t.matches m join t.players p where p.id = :id")
    Collection<Match> findMatches(@Param("id") Long id);
}

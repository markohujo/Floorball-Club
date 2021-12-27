package cz.cvut.fit.tjv.hujomark.project.dao;

import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MatchJpaRepository extends JpaRepository<Match, Long> {

    @Query(value = "SELECT * FROM MATCH m WHERE m.team_id != ?1",
            nativeQuery = true)
    Collection<Match> findMatchesExcept(Long teamId);
}

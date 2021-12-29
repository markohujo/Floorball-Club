package cz.cvut.fit.tjv.hujomark.project.dao;

import cz.cvut.fit.tjv.hujomark.project.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team, Long> {
    @Query(value = "select * from team except select t.* from team t join player_team pt on t.id = pt.team_id where pt.player_id = ?1",
            nativeQuery = true)
    Collection<Team> findTeamsExcept(Long id);
}

package cz.cvut.fit.tjv.hujomark.project.dao;

import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchJpaRepository extends JpaRepository<Match, Long> {}

package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.dao.MatchJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTests {

    @InjectMocks
    MatchService service;

    @Mock
    MatchJpaRepository repository;

    @Test
    public void testUpdateDateTime() {
        // TODO
    }

    @Test
    public void testUpdateEmail() {
        // TODO
    }
}

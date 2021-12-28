package cz.cvut.fit.tjv.hujomark.project.service;

import cz.cvut.fit.tjv.hujomark.project.business.PlayerService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.dao.PlayerJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.hujomark.project.domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AbstractCrudServiceTests {

    @InjectMocks
    PlayerService service;

    @Mock
    PlayerJpaRepository repository;

    @Test
    public void testCreate() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Mockito.when(repository.save(player)).thenReturn(player);
        Assertions.assertEquals(player, service.create(player));
        Mockito.verify(repository, Mockito.times(1)).save(player);
    }

    @Test
    public void testReadAll() {
        Player player1 = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Player player2 = new Player(2L, "Someone", "Else", null, null, new HashSet<>());
        List<Player> players = List.of(player1, player2);
        Mockito.when(repository.findAll()).thenReturn(players);
        Assertions.assertEquals(players, service.readAll());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetOne() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(player));
        var playerReturned = service.readById(1L);
        Assertions.assertTrue(playerReturned.isPresent());
        Assertions.assertEquals(player, playerReturned.get());
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository, Mockito.times(1)).findById(argumentCaptor.capture());
        Assertions.assertEquals(player.getId(), argumentCaptor.getValue());
    }

    @Test
    public void testUpdate() {
        Player nonExistingPlayer = new Player(10L, null, null, null, null, Collections.emptySet());
        Mockito.when(repository.existsById(nonExistingPlayer.getId())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(nonExistingPlayer));
        Mockito.verify(repository, Mockito.never()).save(nonExistingPlayer);

        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Mockito.when(repository.existsById(player.getId())).thenReturn(true);
        service.update(player);
        Mockito.verify(repository, Mockito.times(1)).save(player);
    }

    @Test
    public void testDelete() {
        Player player = new Player(1L, "Marko", "Hujo", null, null, new HashSet<>());
        Mockito.when(repository.findById(player.getId())).thenReturn(Optional.of(player));
        service.deleteById(player.getId());
        Mockito.verify(repository, Mockito.times(1)).deleteById(player.getId());

    }
}

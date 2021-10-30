package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public MatchDto newMatch(@RequestBody MatchDto newMatch) {
        Match match = MatchConverter.toModel(newMatch);
        matchService.create(match);
        return MatchConverter.fromModel(match);
    }

    @GetMapping("/matches")
    public Collection<MatchDto> all() {
        return MatchConverter.fromModelMany(matchService.readAll());
    }

    @GetMapping("/matches/{id}")
    public MatchDto one(@PathVariable Long id) {
        return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
    }

    @PutMapping("/matches/{id}")
    public MatchDto updateDateTime(@PathVariable Long id, @RequestParam LocalDateTime dateTime) {
        matchService.updateDateTime(id, dateTime);
        return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
    }

    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteById(id);
    }
}

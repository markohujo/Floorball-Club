package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST API for Match entity
 *
 * Supported methods:
 *      POST newMatch (/matches)
 *      GET all (/matches)
 *      GET one (/matches/{id})
 *      GET team (/matches/{id}/team)
 *      PUT updateDateTime (/matches/{id})
 *      PUT updateTeam (/matches/{id}/team)
 *      DELETE deleteMatch (/matches/{id})
 */
@RestController
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public MatchDto newMatch(@RequestBody MatchDto newMatchDTO) {
        matchService.create(MatchConverter.toModel(newMatchDTO));
        return one(newMatchDTO.getId());
    }

    @GetMapping("/matches")
    public Collection<MatchDto> all() {
        return MatchConverter.fromModelMany(matchService.readAll());
    }

    @GetMapping("/matches/{id}")
    public MatchDto one(@PathVariable Long id) {
        return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
    }

    @GetMapping("matches/{id}/team")
    public TeamDto team(@PathVariable Long id) {
        return TeamConverter.fromModel(matchService.readById(id).orElseThrow().getTeam());
    }

    @PutMapping("/matches/{id}")
    public MatchDto updateDateTime(@PathVariable Long id, @RequestParam String dateTimeStr) {
        matchService.updateDateTime(id, dateTimeStr);
        return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
    }

    @PutMapping("/matches/{id}/team")
    public MatchDto updateTeam(@PathVariable Long id, @RequestParam Long teamId) {
        matchService.updateTeam(id, teamId);
        return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
    }

    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteById(id);
    }
}

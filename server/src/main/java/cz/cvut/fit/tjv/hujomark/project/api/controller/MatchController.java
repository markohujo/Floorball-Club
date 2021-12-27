package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.api.converter.MatchConverter;
import cz.cvut.fit.tjv.hujomark.project.api.converter.TeamConverter;
import cz.cvut.fit.tjv.hujomark.project.business.MatchService;
import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import cz.cvut.fit.tjv.hujomark.project.domain.Match;
import org.apache.tomcat.jni.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

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

    public MatchController(MatchService matchService, TeamService teamService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public MatchDto newMatch(@RequestBody MatchDto newMatchDTO) {
        try {
            Match match = matchService.create(MatchConverter.toModel(newMatchDTO));
            return one(match.getId());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cannot create match as team with the given teamId was not found");
        }
    }

    @GetMapping("/matches")
    public Collection<MatchDto> all() {
        return MatchConverter.fromModelMany(matchService.readAll());
    }

    @GetMapping("/matches/{id}")
    public MatchDto one(@PathVariable Long id) {
        return MatchConverter.fromModel(matchService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match Not Found")));
    }

    @GetMapping("matches/{id}/team")
    public TeamDto team(@PathVariable Long id) {
        return TeamConverter.fromModel(matchService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match Not Found"))
                .getTeam());
    }

    @GetMapping("matches/except")
    public Collection<MatchDto> exceptTeamWithId(@RequestParam Long teamId) {
        return MatchConverter.fromModelMany(matchService.exceptTeamWithId(teamId));
    }

    @PutMapping("/matches/{id}/update")
    public MatchDto update(@PathVariable Long id, @RequestBody MatchDto match) {
        try {
            matchService.updateDateTime(id, match.dateTime.toString());
            matchService.updateTeam(id, match.teamId);
            return MatchConverter.fromModel(matchService.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/matches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMatch(@PathVariable Long id) {
        try {
            matchService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match Not Found");
        }
    }
}

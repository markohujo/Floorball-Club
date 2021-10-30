package cz.cvut.fit.tjv.hujomark.project.api.controller;

import cz.cvut.fit.tjv.hujomark.project.business.TeamService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // TODO POST newTeam
    // TODO GET all
    // TODO GET one
    // TODO GET players
    // TODO GET matches
    // TODO PUT addPlayer
    // TODO PUT addMatch
    // TODO PUT removePlayer
    // TODO DELETE deleteTeam

}

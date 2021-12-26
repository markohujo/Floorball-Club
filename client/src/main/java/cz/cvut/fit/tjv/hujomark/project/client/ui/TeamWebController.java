package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teams")
public class TeamWebController {

    private final TeamClient teamClient;

    public TeamWebController(TeamClient teamClient) {
        this.teamClient = teamClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamClient.readAll());
        return "teams";
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        teamClient.setCurrentId(id);
        model.addAttribute("team", teamClient.readOne(id));
        return "teamDetails";
    }

    @GetMapping("/players")
    public String players(@RequestParam Long id, Model model) {
        model.addAttribute("players", teamClient.players(id));
        model.addAttribute("team", teamClient.readOne(id));
        return "teamPlayers";
    }

    @GetMapping("/matches")
    public String matches(@RequestParam Long id, Model model) {
        model.addAttribute("matches", teamClient.matches(id));
        model.addAttribute("team", teamClient.readOne(id));
        return "teamMatches";
    }
}

package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teams")
public class TeamWenController {

    private final TeamClient teamClient;

    public TeamWenController(TeamClient teamClient) {
        this.teamClient = teamClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamClient.readAll());
        return "teams";
    }
}

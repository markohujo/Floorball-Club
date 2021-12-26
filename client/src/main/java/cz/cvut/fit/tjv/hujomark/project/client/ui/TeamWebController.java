package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.PlayerClient;
import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.TeamDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
public class TeamWebController {

    private final TeamClient teamClient;

    private final PlayerClient playerClient;

    public TeamWebController(TeamClient teamClient, PlayerClient playerClient) {
        this.teamClient = teamClient;
        this.playerClient = playerClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamClient.readAll());
        return "teams";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("team", new TeamDto());
        return "teamAdd";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute PlayerDto player, Model model) {
        teamClient.create(player).subscribe();
        return "redirect:/teams";
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        teamClient.setCurrentId(id);
        model.addAttribute("team", teamClient.readOne(id));
        model.addAttribute("allPlayers", playerClient.readAll());
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

    @PostMapping("/addPlayer")
    public String addPlayer (@ModelAttribute TeamDto team, Model model) {
        teamClient.addPlayer(team.tmpId);
        return "redirect:/teams";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        teamClient.delete(id);
        return "redirect:/teams";
    }
}

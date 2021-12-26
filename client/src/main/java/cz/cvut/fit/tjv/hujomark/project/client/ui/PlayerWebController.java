package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.PlayerClient;
import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
public class PlayerWebController {

    private final PlayerClient playerClient;

    private final TeamClient teamClient;

    public PlayerWebController(PlayerClient playerClient, TeamClient teamClient) {
        this.playerClient = playerClient;
        this.teamClient = teamClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("players", playerClient.readAll());
        return "players";
    }

    @GetMapping("/teams")
    public String teams(@RequestParam Long id, Model model) {
        model.addAttribute("teams", playerClient.teams(id));
        model.addAttribute("player", playerClient.readOne(id));
        return "playerTeams";
    }

    @GetMapping("/matches")
    public String matches(@RequestParam Long id, Model model) {
        model.addAttribute("matches", playerClient.matches(id));
        model.addAttribute("player", playerClient.readOne(id));
        return "playerMatches";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("player", new PlayerDto());
        return "playerAdd";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute PlayerDto player, Model model) {
        playerClient.create(player).subscribe();
        return "redirect:/players";
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        playerClient.setCurrentId(id);
        model.addAttribute("player", playerClient.readOne(id));
        model.addAttribute("allTeams", teamClient.readAll());
        model.addAttribute("playerTeams", playerClient.teams(id));
        return "playerDetails";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute PlayerDto player, Model model) {
        playerClient.update(player.email);
        return "redirect:/players";
    }

    @PostMapping("/addToTeam")
    public String addToTeam (@ModelAttribute PlayerDto player, Model model) {
        playerClient.addToTeam(player.tmpTeamId);
        return "redirect:/players";
    }

    @PostMapping("/removeFromTeam")
    public String removeFromTeam (@ModelAttribute PlayerDto player, Model model) {
        playerClient.removeFromTeam(player.tmpTeamId);
        return "redirect:/players";
    }

    @PostMapping("/createMatch")
    public String createMatchForEachTeam (Model model) {
        System.out.println("here: create match");
        System.out.println(playerClient.getCurrentId());
        playerClient.createMatchForEachTeam();
        return "redirect:/players";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        playerClient.delete(id);
        return "redirect:/players";
    }
}

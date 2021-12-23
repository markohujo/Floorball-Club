package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.PlayerClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/players")
public class PlayerWebController {
    private final PlayerClient playerClient;


    public PlayerWebController(PlayerClient playerClient) {
        this.playerClient = playerClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("players", playerClient.readAll());
        return "players";
    }

    @GetMapping("/teams")
    public String teams(@RequestParam Long id, Model model) {
        model.addAttribute("teams", playerClient.teams(id));
        return "teams";
    }

    @GetMapping("/matches")
    public String matches(@RequestParam Long id, Model model) {
        // TODO
        return "matches";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        playerClient.delete(id);
        return "redirect:/players";
    }
}

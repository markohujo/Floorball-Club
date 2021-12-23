package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.PlayerClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

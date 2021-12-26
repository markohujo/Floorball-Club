package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.PlayerClient;
import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("matches", playerClient.matches(id));
        return "matches";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("player", new PlayerDto());
        return "playerAdd";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute PlayerDto player, Model model) {
        System.out.println("here" + player.name);
        playerClient.create(player).subscribe();
        return "redirect:/players";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {
        playerClient.setCurrentId(id);
        model.addAttribute("player", playerClient.readOne(id));
        return "playerEdit";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute PlayerDto player, Model model) {
        playerClient.update(player.email);
        return "redirect:/players";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        playerClient.delete(id);
        return "redirect:/players";
    }
}

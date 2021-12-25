package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.MatchClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/matches")
public class MatchWebController {

    private final MatchClient matchClient;

    public MatchWebController(MatchClient matchClient) {
        this.matchClient = matchClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("matches", matchClient.readAll());
        return "matches";
    }
}

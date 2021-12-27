package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.MatchClient;
import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import cz.cvut.fit.tjv.hujomark.project.client.model.MatchDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/matches")
public class MatchWebController {

    private final MatchClient matchClient;

    private final TeamClient teamClient;

    public MatchWebController(MatchClient matchClient, TeamClient teamClient) {
        this.matchClient = matchClient;
        this.teamClient = teamClient;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("matches", matchClient.readAll());
        return "matches";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("match", new MatchDto());
        model.addAttribute("allTeams", teamClient.readAll());
        return "matchAdd";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute MatchDto match, Model model) {
        match.setDateTime(LocalDateTime.of(match.date, match.time));
        matchClient.create(match).subscribe();
        return "redirect:/matches";
    }
}

package cz.cvut.fit.tjv.hujomark.project.client.ui;

import cz.cvut.fit.tjv.hujomark.project.client.data.MatchClient;
import cz.cvut.fit.tjv.hujomark.project.client.data.TeamClient;
import cz.cvut.fit.tjv.hujomark.project.client.model.MatchDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

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

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute PlayerDto player, Model model) {
        matchClient.update(player);
        return "redirect:/matches";
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

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        matchClient.setCurrentId(id);
        model.addAttribute("match", matchClient.readOne(id));
        model.addAttribute("team", matchClient.team(id));
        model.addAttribute("allTeams", teamClient.readAll());
        return "matchDetails";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        matchClient.delete(id);
        return "redirect:/matches";
    }
}

package cz.cvut.fit.tjv.hujomark.project.client.data;

import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.TeamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class TeamClient {

    private final WebClient webClient;

    public TeamClient(@Value("${backend_url}") String backendUrl) {
        this.webClient = WebClient.create(backendUrl + "/teams");;
    }

    public Flux<TeamDto> readAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(TeamDto.class);
    }
}
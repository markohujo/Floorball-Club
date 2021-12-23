package cz.cvut.fit.tjv.hujomark.project.client.data;

import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.TeamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class PlayerClient {
    private final WebClient webClient;

    public PlayerClient(@Value("${backend_url}") String backendUrl) {
        this.webClient = WebClient.create(backendUrl + "/players");
    }

    public Flux<PlayerDto> readAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(PlayerDto.class);
    }

    public Flux<TeamDto> teams(Long id) {
        return webClient.get()
                .uri("/{id}/teams", id)
                .retrieve()
                .bodyToFlux(TeamDto.class);
    }

    public void delete(Long id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}

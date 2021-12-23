package cz.cvut.fit.tjv.hujomark.project.client.data;

import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class PlayerClient {
    private final WebClient playerWebClient;

    public PlayerClient(@Value("${backend_url}") String backendUrl) {
        this.playerWebClient = WebClient.create(backendUrl + "/players");
    }

    public Flux<PlayerDto> readAll() {
        return playerWebClient.get()
                .retrieve()
                .bodyToFlux(PlayerDto.class);
    }
}

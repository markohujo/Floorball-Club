package cz.cvut.fit.tjv.hujomark.project.client.data;

import cz.cvut.fit.tjv.hujomark.project.client.model.MatchDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.PlayerDto;
import cz.cvut.fit.tjv.hujomark.project.client.model.TeamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MatchClient {

    private final WebClient webClient;

    public MatchClient(@Value("${backend_url}") String backendUrl) {
        this.webClient = WebClient.create(backendUrl + "/matches");;
    }

    public Flux<MatchDto> readAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(MatchDto.class);
    }

    public Mono<MatchDto> create(MatchDto match) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(match)
                .retrieve()
                .bodyToMono(MatchDto.class);
    }

    public void delete(Long id) {
        webClient.delete()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}

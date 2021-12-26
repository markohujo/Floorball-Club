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

import java.util.Objects;

@Component
public class PlayerClient {
    private final WebClient webClient;
    private Long currentId;

    public PlayerClient(@Value("${backend_url}") String backendUrl) {
        this.webClient = WebClient.create(backendUrl + "/players");
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long id) {
        this.currentId = id;
    }

    public Mono<PlayerDto> create(PlayerDto player) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(player)
                .retrieve()
                .bodyToMono(PlayerDto.class);
    }

    public Flux<PlayerDto> readAll() {
        return webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(PlayerDto.class);
    }

    public Mono<PlayerDto> readOne(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PlayerDto.class);
    }

    public Flux<TeamDto> teams(Long id) {
        return webClient.get()
                .uri("/{id}/teams", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TeamDto.class);
    }

    public Flux<MatchDto> matches(Long id) {
            return webClient.get()
                    .uri("/{id}/matches", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(MatchDto.class);
    }

    public void delete(Long id) {
        webClient.delete()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    public void update(String email) {
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .queryParam("email", "{email}")
                        .build(currentId, email))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {}, e -> {});
    }

    public void addToTeam(Long newTeamId) {
        System.out.println(currentId);
        System.out.println(newTeamId);
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/teams/add")
                        .queryParam("teamId", "{teamId}")
                        .build(currentId, newTeamId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity().subscribe();
    }
}

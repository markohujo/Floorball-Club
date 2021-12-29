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
public class TeamClient {

    private final WebClient webClient;

    private Long currentId;

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public TeamClient(@Value("${backend_url}") String backendUrl) {
        this.webClient = WebClient.create(backendUrl + "/teams");;
    }

    public Mono<TeamDto> create(PlayerDto player) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(player)
                .retrieve()
                .bodyToMono(TeamDto.class);
    }

    public Flux<TeamDto> readAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(TeamDto.class);
    }

    public Mono<TeamDto> readOne(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TeamDto.class);
    }

    public Flux<PlayerDto> players(Long id) {
        return webClient.get()
                .uri("/{id}/players", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(PlayerDto.class);
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

    public void addPlayer(Long playerId) {
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/players/add")
                        .queryParam("player", "{player}")
                        .build(currentId, playerId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity().subscribe();
    }

    public void removePLayer(Long playerId) {
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/players/remove")
                        .queryParam("player", "{player}")
                        .build(currentId, playerId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity().subscribe();
    }

    public void addMatch(Long matchId) {
        System.out.println("here here");
        System.out.println(currentId);
        System.out.println(matchId);
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/matches/add")
                        .queryParam("match", "{match}")
                        .build(currentId, matchId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity().subscribe();
    }

    public void removeMatch(Long matchId) {
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/matches/remove")
                        .queryParam("match", "{match}")
                        .build(currentId, matchId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity().subscribe();
    }

    public Flux<MatchDto> availableMatches(Long teamId) {
        return webClient.get()
                .uri("/{id}/matches/available", teamId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(MatchDto.class);
    }

    public Flux<PlayerDto> availablePlayers(Long teamId) {
        return webClient.get()
                .uri("/{id}/players/available", teamId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(PlayerDto.class);
    }
}

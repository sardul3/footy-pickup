package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.Team;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class UpdateHandler {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final MatchService matchService;

    public UpdateHandler(PlayerService playerService, TeamService teamService, MatchService matchService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.matchService = matchService;
    }

    public Mono<ServerResponse> updatePlayer(ServerRequest request) {
        var res = request.bodyToFlux(Player.class)
                .flatMap(player -> this.playerService.updatePlayer(extractId(request), player));
        return defaultWriteResponseForPlayer(res);
    }

    public Mono<ServerResponse> updateTeam(ServerRequest request) {
        var res = request.bodyToFlux(Team.class)
                .flatMap(team -> this.teamService.updateTeam(extractId(request), team));
        return defaultWriteResponseForTeam(res);
    }

    public Mono<ServerResponse> updateMatch(ServerRequest request) {
        var res = request.bodyToFlux(Match.class)
                .flatMap(match -> this.matchService.updateMatch(extractId(request), match));
        return defaultWriteResponseForMatch(res);
    }

    private static Mono<ServerResponse> defaultWriteResponseForPlayer(Publisher<Player> player) {
        return Mono
                .from(player)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/profiles/" + p.getId()))
                        .bodyValue(p)
                );
    }

    private static Mono<ServerResponse> defaultWriteResponseForMatch(Publisher<Match> match) {
        return Mono
                .from(match)
                .flatMap(m -> ServerResponse
                        .created(URI.create("/profiles/" + m.getId()))
                        .bodyValue(m)
                );
    }

    private static Mono<ServerResponse> defaultWriteResponseForTeam(Publisher<Team> team) {
        return Mono
                .from(team)
                .flatMap(t -> ServerResponse
                        .created(URI.create("/profiles/" + t.getId()))
                        .bodyValue(t)
                );
    }

    private static String extractId(ServerRequest r) {
        return r.pathVariable("id");
    }
}

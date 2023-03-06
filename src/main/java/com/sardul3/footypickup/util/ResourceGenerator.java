package com.sardul3.footypickup.util;

import com.sardul3.footypickup.controller.PlayerController;
import com.sardul3.footypickup.domain.Player;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public class ResourceGenerator {
    private ResourceGenerator() {}

    public static Mono<EntityModel<Player>> getPlayerResource(Player player) {
        Class<PlayerController> controllerClass = PlayerController.class;
        Mono<Link> createdPlayerLink = linkTo(methodOn(controllerClass).createPlayerPost(player)).withRel("player").toMono();
        Mono<Link> allPlayersLink = linkTo(methodOn(controllerClass).getAllPlayers()).withRel("all_players").toMono();
        Mono<List<Link>> entityLinkList = Flux.concat(createdPlayerLink, allPlayersLink).collectList();
        Mono<EntityModel<Player>> entityMono = entityLinkList.map(links -> EntityModel.of(player, links));
        return entityMono;
    }

    public static Mono<EntityModel<Flux<Player>>> getPlayersResource(Flux<Player> players) {
        Class<PlayerController> controllerClass = PlayerController.class;
        Mono<Link> allPlayersLink = linkTo(methodOn(controllerClass).getAllPlayers()).withRel("all_players").toMono();
        Mono<EntityModel<Flux<Player>>> entityMono = allPlayersLink.map(links -> EntityModel.of(players, links));
        return entityMono;
    }
}

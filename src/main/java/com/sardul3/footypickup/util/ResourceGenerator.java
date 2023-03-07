package com.sardul3.footypickup.util;

import com.sardul3.footypickup.controller.MatchController;
import com.sardul3.footypickup.controller.PlayerController;
import com.sardul3.footypickup.controller.TeamController;
import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.dto.CreateTeamRequest;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

//TODO: research in depth on HATEAOS
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

    public static Mono<EntityModel<Team>> getTeamResource(Team team) {
        Class<TeamController> controllerClass = TeamController.class;
        ModelMapper modelMapper = new ModelMapper();
        Mono<Link> createdTeamLink = linkTo(methodOn(controllerClass)
                                        .createNewTeam(modelMapper.map(team, CreateTeamRequest.class)))
                                        .withRel("team").toMono();
        Mono<List<Link>> entityLinkList = Flux.concat(createdTeamLink).collectList();
        Mono<EntityModel<Team>> entityMono = entityLinkList.map(links -> EntityModel.of(team, links));
        return entityMono;
    }

    public static Mono<EntityModel<Match>> getMatchResource(Match match) {
        Class<MatchController> controllerClass = MatchController.class;
        Mono<Link> createdMatchLink = linkTo(methodOn(controllerClass)
                .createMatchPost(match))
                .withRel("match").toMono();
        Mono<List<Link>> entityLinkList = Flux.concat(createdMatchLink).collectList();
        Mono<EntityModel<Match>> entityMono = entityLinkList.map(links -> EntityModel.of(match, links));
        return entityMono;
    }
}

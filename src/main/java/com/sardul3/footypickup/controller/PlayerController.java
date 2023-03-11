package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.service.PlayerService;
import com.sardul3.footypickup.util.ResourceGenerator;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Player>>>> createPlayerPost(@Valid @RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(ResourceGenerator::getPlayerResource)
                .map(playerEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(playerEntityModel));
    }

    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<Flux<Player>>> getAllPlayers() {
        Flux<Player> players = playerService.getAllPlayers();
        ResponseEntity<Flux<Player>> response = ResponseEntity.status(HttpStatus.OK).body(players);
        return Mono.just(response);
    }
}

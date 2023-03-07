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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /*
        Spring provides fully non-blocking reading and streaming if the input to the controller is a reactive type.
        TODO: replace Player with Mono<Player>
        NOTE: validation will not be triggered if the passed in value is never used in the chain of operations in
              spring-webflux
     */
    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Player>>>> createPlayerPost(@Valid @RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(playerMono -> ResourceGenerator.getPlayerResource(playerMono))
                .map(playerEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(playerEntityModel));
    }

    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<Flux<Player>>> getAllPlayers() {
        Flux<Player> players = playerService.getAllPlayers();
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(players);
        return Mono.just(response);
    }
}

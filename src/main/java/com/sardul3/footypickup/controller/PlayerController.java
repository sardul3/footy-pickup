package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.service.PlayerService;
import com.sardul3.footypickup.util.ResourceGenerator;
import io.micrometer.core.annotation.Timed;
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

    @Timed(value="player.create", description = "time taken to create a new player")
    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Player>>>> createPlayerPost(@Valid @RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(ResourceGenerator::getPlayerResource)
                .map(playerEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(playerEntityModel));
    }

    @GetMapping
    @ResponseBody
    @Timed(value="player.get.all", description = "time taken to get all the players")
    public Mono<ResponseEntity<Flux<Player>>> getAllPlayers() {
        Flux<Player> players = playerService.getAllPlayers();
        ResponseEntity<Flux<Player>> response = ResponseEntity.status(HttpStatus.OK).body(players);
        return Mono.just(response);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Timed(value="player.get", description = "time taken to retrieve a player")
    public Mono<ResponseEntity<Mono<EntityModel<Player>>>> getPlayer(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(ResourceGenerator::getPlayerResource)
                .map(playerEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(playerEntityModel));

    }

    @DeleteMapping("/{id}")
    @Timed(value="player.delete", description = "time taken to delete a player")
    public Mono<Void> deletePlayer(@PathVariable String id) {
        return playerService.deletePlayer(id);}
}

package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
//import com.sardul3.footypickup.dto.CreatePlayerRequest;
import com.sardul3.footypickup.service.PlayerService;
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
    public Mono<ResponseEntity<Player>> createPlayerPost(@Valid @RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(play -> ResponseEntity.status(HttpStatus.CREATED).body(play));
    }

    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<Flux<Player>>> getAllPlayers() {
        var players = playerService.getAllPlayers();
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(players);
        return Mono.just(response);
    }
}

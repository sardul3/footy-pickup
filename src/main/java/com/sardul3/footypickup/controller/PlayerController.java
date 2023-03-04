package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
//import com.sardul3.footypickup.dto.CreatePlayerRequest;
import com.sardul3.footypickup.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public Mono<ResponseEntity<Player>> createPlayerPost(@RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(play -> ResponseEntity.status(HttpStatus.CREATED).body(play));
    }
}

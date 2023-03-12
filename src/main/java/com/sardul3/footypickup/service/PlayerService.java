package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.exception.custom.EmptyResourceCollectionException;
import com.sardul3.footypickup.exception.custom.ResourceNotFoundException;
import com.sardul3.footypickup.repo.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<Player> addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll()
                .switchIfEmpty(Mono.error(new EmptyResourceCollectionException("No Players present in DB")));
    }

    public Mono<Player>  getPlayerById(String id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player not found")));
    }

    public Mono<Void> deletePlayer(String id) {
        return playerRepository.deleteById(id).log();
    }
}

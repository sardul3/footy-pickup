package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.repo.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<Player> addPlayer(Player player) {
        return playerRepository.save(player);
    }
}

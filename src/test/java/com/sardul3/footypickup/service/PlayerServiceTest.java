package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.repo.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService playerService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addPlayer() {
        var player = new Player(null, "S", "P", 7, PlayerPosition.ST, false);
        when(playerRepository.save(player)).thenReturn(Mono.just(player));
        StepVerifier.create(playerService.addPlayer(player))
                .expectNextCount(1)
                .verifyComplete();
    }
}
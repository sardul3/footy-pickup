package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.service.MatchService;
import com.sardul3.footypickup.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MatchControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MatchService matchService;

    @Test
    void startMatch() {
    }

    @Test
    void createMatchPost() {
        Match match = Match.builder()
                .numberOfPlayersPerSide(5)
                .gameMinutes(90)
                .build();

        webTestClient
                .post()
                .uri("/v1/matches")
                .bodyValue(match)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Match.class)
                .consumeWith(matchEntityExchangeResult -> {
                    assert matchEntityExchangeResult.getResponseBody().getGameMinutes() == 90;
                    assert matchEntityExchangeResult.getResponseBody().isGameStarted() == false;
                });
    }
}
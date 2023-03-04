package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.service.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class PlayerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PlayerService playerService;

    static String BASE_URL = "/v1/players";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPlayerPost() {
       var player = new Player(null, "John", "B",
                13, PlayerPosition.RB, false);

       webTestClient
               .post()
               .uri(BASE_URL)
               .bodyValue(player)
               .exchange()
               .expectStatus()
               .isCreated()
               .expectBody(Player.class)
               .consumeWith(entityExchangeResult -> {
                   assert entityExchangeResult.getResponseBody().getId() != null;
               });
    }

    @Test
    void createPlayer_withInvalidShirtNumber_shouldThrowBadRequest() {
        // shirt number should be in the range of 1 to 100
        var player = new Player(null, "John", "B",
                125, PlayerPosition.RB, false);

        webTestClient
                .post()
                .uri(BASE_URL)
                .bodyValue(player)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    assert stringEntityExchangeResult.getResponseBody() != null;
                    assert stringEntityExchangeResult.getResponseBody().contains("within 1 - 99");
                });
    }

    @Test
    void createPlayer_withEmptyFirstName_shouldThrowBadRequest() {
        // First Name cannot be empty
        var player = new Player(null, "", "B",
                25, PlayerPosition.RB, false);

        webTestClient
                .post()
                .uri(BASE_URL)
                .bodyValue(player)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    assert stringEntityExchangeResult.getResponseBody() != null;
                    assert stringEntityExchangeResult.getResponseBody().contains("first name must have at least one character");
                });
    }
}
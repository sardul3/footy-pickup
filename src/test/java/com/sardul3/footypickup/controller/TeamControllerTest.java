package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.dto.CreateTeamRequest;
import com.sardul3.footypickup.service.TeamService;
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
class TeamControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TeamService teamService;

    static String BASE_URL = "/v1/teams";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createNewTeam() {
        CreateTeamRequest request = new CreateTeamRequest("BARCA");

        webTestClient
                .post()
                .uri(BASE_URL)
                .bodyValue(request)
                .exchange()
                .expectBody(Team.class)
                .consumeWith(teamEntityExchangeResult -> {
                    assert teamEntityExchangeResult.getResponseBody().getId() != null;
                    assert teamEntityExchangeResult.getResponseBody().getName().equalsIgnoreCase("BARCA");
                });
    }

    @Test
    void createNewTeam_withEmptyName_shouldThrowError() {
        CreateTeamRequest request = new CreateTeamRequest(null);

        webTestClient
                .post()
                .uri(BASE_URL)
                .bodyValue(request)
                .exchange()
                .expectBody(String.class)
                .consumeWith(teamEntityExchangeResult -> {
                    assert teamEntityExchangeResult.getResponseBody() != null;
                    assert teamEntityExchangeResult.getResponseBody().contains("team name cannot be empty");
                });
    }

    @Test
    void getAllTeams() {
        webTestClient
                .get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    assert entityExchangeResult.getResponseBody()!=null;
                });
    }
}
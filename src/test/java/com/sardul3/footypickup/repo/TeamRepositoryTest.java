package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.domain.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class TeamRepositoryTest {

    @Autowired
    TeamRepository repository;

    @BeforeEach
    void setUp() {
        var lion_players = List.of(
                new Player(null, "Sagar", "Poudel", 3, PlayerPosition.LB.name(), true),
                new Player(null, "Anup", "Ghimire", 1, PlayerPosition.GK.name(), false)
        );

        var tiger_players = List.of(
                new Player(null, "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false),
                new Player(null, "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false)
        );

        var teams = List.of(
          new Team(null, "Tigers FC", tiger_players),
          new Team(null,"Lions FC", lion_players)
        );
        repository.saveAll(teams).blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void saveShouldCreateANewTeam() {
        Team team = new Team(null, "Reds FC", null);
        StepVerifier.create(repository.save(team))
                .assertNext(createdTeam -> {
                    assert createdTeam.getId() != null;
                })
                .verifyComplete();
    }
}
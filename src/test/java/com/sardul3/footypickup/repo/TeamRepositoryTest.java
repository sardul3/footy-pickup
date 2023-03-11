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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class TeamRepositoryTest {

    @Autowired
    TeamRepository repository;

    @BeforeEach
    void setUp() {
        var lion_players = Set.of(
                new Player(null, "Sagar", "Poudel", 3, PlayerPosition.LB.name(), true, null),
                new Player(null, "Anup", "Ghimire", 1, PlayerPosition.GK.name(), false, null)
        );

        var tiger_players = Set.of(
                new Player(null, "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player(null, "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null)
        );

        var teams = Set.of(
          new Team(null, "Tigers FC", "TFC", tiger_players),
          new Team(null,"Lions FC", "LFC", lion_players)
        );
        repository.saveAll(teams).blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void saveShouldCreateANewTeam() {
        Team team = new Team("adasd", "Reds FC", "RFC", Set.of(
                new Player("fadjl", "Sagar", "Poudel", 3, PlayerPosition.LB.name(), true, new ArrayList<>())
                ));
        StepVerifier.create(repository.save(team))
                .assertNext(createdTeam -> {
                    assert createdTeam.getId() != null;
                })
                .verifyComplete();
    }
}
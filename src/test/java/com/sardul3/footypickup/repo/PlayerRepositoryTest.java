package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        var players = List.of(
                new Player(null, "Sagar", "Poudel", 3, PlayerPosition.LB.name(), true),
                new Player(null, "Anup", "Ghimire", 1, PlayerPosition.GK.name(), false),
                new Player(null, "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false),
                new Player(null, "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false)
                );
        playerRepository.saveAll(players).blockLast();
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll().block();
    }

    @Test
    void saveShouldCreateANewPlayer() {
        var newPlayer= playerRepository.save(new Player(null, "John", "B",
                13, PlayerPosition.RB.name(), false));

        StepVerifier.create(newPlayer)
                .assertNext(player -> {
                    assert player.getPlayerPosition() == PlayerPosition.RB.toString();
                })
                .verifyComplete();


    }
}
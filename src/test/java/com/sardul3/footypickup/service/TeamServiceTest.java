package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.dto.CreateTeamRequest;
import com.sardul3.footypickup.repo.PlayerRepository;
import com.sardul3.footypickup.repo.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ModelMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {


    TeamService service;

    @Mock
    TeamRepository teamRepository;

    @Mock
    PlayerRepository playerRepository;
    @BeforeEach
    void setUp() {
        service = new TeamService(teamRepository, playerRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createNewTeam() {
        Team team = new Team(null,"LFC", null );
        CreateTeamRequest createTeamRequest = new ModelMapper().map(team, CreateTeamRequest.class);
        when(teamRepository.save(any())).thenReturn(Mono.just(team));
        StepVerifier.create(service.createNewTeam(createTeamRequest))
                .expectNextCount(1)
                .assertNext(team1 -> {
                   assert team1.getId() != null;
                });
    }

    @Test
    void addPlayerToExistingTeam() {
        var player = new Player("abc", "S", "P", 7, PlayerPosition.ST.name(), false);

        when(playerRepository.findById(any(String.class))).thenReturn(Mono.just(player));
        when(teamRepository.findById(any(String.class))).thenReturn(Mono.just(new Team("def", "LFC", null)));

        StepVerifier.create(service.addPlayerToExistingTeam("def", "abc"))
                .assertNext(team -> {
                    assert team.getPlayers().size() !=0;
                })
                .expectNextCount(1);
    }

    @Test
    void getAllTeams() {
        when(teamRepository.findAll()).thenReturn(Flux.just(new Team(null, "LFC", null),
                new Team(null, "LAFC", null)));
        StepVerifier.create(service.getAllTeams())
                .expectNextCount(2)
                .verifyComplete();
    }
}
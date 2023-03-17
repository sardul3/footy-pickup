package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.exception.custom.MatchHasInvalidNumberOfTeamsException;
import com.sardul3.footypickup.exception.custom.ResourceAlreadyExistsException;
import com.sardul3.footypickup.exception.custom.TeamDoesNotHaveMinimumNumberOfPlayersException;
import com.sardul3.footypickup.repo.MatchRepository;
import com.sardul3.footypickup.repo.PlayerRepository;
import com.sardul3.footypickup.repo.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class MatchServiceTest {

    @Mock
    MatchRepository matchRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    ApplicationEventPublisher publisher;

    @InjectMocks
    MatchService matchService;

    @Test
    void createNewFootballMatch() {
        var match = Match.builder().numberOfPlayersPerSide(5).build();
        when(matchRepository.save(any())).thenReturn(Mono.just(match));

        StepVerifier.create(matchService.createNewFootballMatch(match))
                .assertNext(createdMatch -> {
                    assert createdMatch.getNumberOfPlayersPerSide() == 5;
                    assert createdMatch.isGameStarted() == false;
                })
                .verifyComplete();
    }

    @Test
    void startMatch_with0TeamShould_resultInError() {
        when(matchRepository.findById(anyString()))
                .thenReturn(Mono.just(
                        Match.builder().id("matchId").gameMinutes(60).location("LIV").build()
                ));

        StepVerifier.create(matchService.startMatch("id"))
                .expectNextCount(0)
                .expectError()
                .verify();
    }

    @Test
    void startMatch_withTwoTeamsShould_withMinimumPlayers_resultInSuccess() {
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null),
                new Player("p3", "Rahul", "G", 10, PlayerPosition.CM.name(), false, null)
        );

        var lion_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 15, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 1, PlayerPosition.CM.name(), false, null),
                new Player("p3", "Rahul", "G", 10, PlayerPosition.CM.name(), false, null)
        );

        var tiger_team = new Team("tiger-team", "Tigers FC", "TFC", tiger_players);
        var lion_team = new Team("lion-team", "Lion FC", "LFC", lion_players);

        var match = Match.builder().id("matchId").gameMinutes(60).location("LIV").teams(Set.of(tiger_team, lion_team))
                .build();
        when(matchRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(match));

        when(matchRepository.save(any()))
                .thenReturn(Mono.just(match));

        StepVerifier.create(matchService.startMatch("matchId"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void startMatch_withTwoTeamsShould_withLessThanMinimumPlayers_resultInError() {
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null)
        );

        var lion_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 15, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 1, PlayerPosition.CM.name(), false, null),
                new Player("p3", "Rahul", "G", 10, PlayerPosition.CM.name(), false, null)
        );

        var tiger_team = new Team("tiger-team", "Tigers FC", "TFC", tiger_players);
        var lion_team = new Team("lion-team", "Lion FC", "LFC", lion_players);

        var match = Match.builder().id("matchId").gameMinutes(60).location("LIV")
                .teams(Set.of(tiger_team, lion_team)).numberOfPlayersPerSide(3)
                .build();

        when(matchRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(match));

        StepVerifier.create(matchService.startMatch("matchId"))
                .expectError(TeamDoesNotHaveMinimumNumberOfPlayersException.class).verify();
    }

    @Test
    void startMatch_withLessThanTwoTeamsShould_resultInError() {

        var lion_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 15, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 1, PlayerPosition.CM.name(), false, null),
                new Player("p3", "Rahul", "G", 10, PlayerPosition.CM.name(), false, null)
        );

        var lion_team = new Team("lion-team", "Lion FC", "LFC", lion_players);

        var match = Match.builder().id("matchId").gameMinutes(60).location("LIV")
                .teams(Set.of(lion_team)).numberOfPlayersPerSide(3)
                .build();

        when(matchRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(match));

        StepVerifier.create(matchService.startMatch("matchId"))
                .expectError(MatchHasInvalidNumberOfTeamsException.class).verify();
    }


    @Test
    void getAllFootballMatches() {
        when(matchRepository.findAll()).thenReturn(Flux.just(
                Match.builder().gameMinutes(60).location("LIV").build(),
                Match.builder().location("EVE").numberOfPlayersPerSide(3).build()
        ));

        StepVerifier.create(matchService.getAllFootballMatches())
                .assertNext(firstMatch -> {
                   assert firstMatch.getGameMinutes() == 60;
                })
                .assertNext(secondMatch -> {
                    assert secondMatch.getLocation().equalsIgnoreCase("EVE");
                })
                .verifyComplete();
    }

    @Test
    void addteamToExistingMatch() {
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null)
        );

        when(matchRepository.findById(anyString())).thenReturn(Mono.just(
                Match.builder().id("matchId").gameMinutes(60).location("LIV").build()
        ));

        when(teamRepository.findById(anyString())).thenReturn(Mono.just(
                new Team("teamId", "Tigers FC","TFC", tiger_players)
        ));

        StepVerifier.create(matchService.addteamToExistingMatch("matchId", "teamId"))
                .assertNext(newMatch -> {
                    assert newMatch.getTeams().size() == 1;
                    assert newMatch.getGameMinutes() == 60;
                });
    }

    @Test
    void addteamToExistingMatch_shouldFailAfter2SuccessfulAdditions() {
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null)
        );

        when(matchRepository.findById(anyString())).thenReturn(Mono.just(
                Match.builder().id("matchId").gameMinutes(60).location("LIV").build()
        ));

        when(teamRepository.findById(anyString())).thenReturn(Mono.just(
                new Team("teamId", "Tigers FC", "TFC", tiger_players)
        ));

        matchService.addteamToExistingMatch("matchId", new Team("teamId", "Tigers FC", "TFC", tiger_players).getId());
        matchService.addteamToExistingMatch("matchId", new Team("teamId", "Tigers FC", "TFC", tiger_players).getId());

        StepVerifier.create(matchService.addteamToExistingMatch("matchId", "teamId"))
                .assertNext(newMatch -> {
                    assert newMatch.getTeams().size() == 1;
                    assert newMatch.getGameMinutes() == 60;
                });
    }

    @Test
    void testAddteamToExistingMatch_with2TeamsShould_succeed() {
        final int DEFAULT_GAME_TIME = 90;
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null),
                new Player("p3", "Rahul", "G", 11, PlayerPosition.CM.name(), false, null)
        );

        when(matchRepository.findById(anyString())).thenReturn(Mono.just(
                Match.builder().id("matchId").gameMinutes(60).location("LIV").build()
        ));

        when(teamRepository.findById(anyString())).thenReturn(Mono.just(
                new Team("teamId", "Tigers FC", "TFC", tiger_players)));

        matchService.addteamToExistingMatch("matchId", new Team("teamId", "Tigers FC", "TFC", tiger_players).getId());
        StepVerifier.create(matchService.addteamToExistingMatch("matchId", new Team("teamId1", "Tigeress FC", "FTFC", tiger_players).getId()))
                .assertNext(match -> {
                    assert match.getNumberOfPlayersPerSide() == 3;
                    assert match.getGameMinutes() != DEFAULT_GAME_TIME;
                })
                .expectNextCount(1);
    }
}
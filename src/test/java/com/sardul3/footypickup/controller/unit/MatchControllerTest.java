package com.sardul3.footypickup.controller.unit;

import com.sardul3.footypickup.controller.MatchController;
import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @Mock
    MatchService matchService;

    @InjectMocks
    MatchController controller;

    @Test
    void startMatch_shouldSetMatchStarted_toTrue() {
        Match match = Match.builder().numberOfPlayersPerSide(5).build();
        match.setGameStarted(true);
        when(matchService.startMatch(any())).thenReturn(Mono.just(match));

        StepVerifier.create(controller.startMatch("matchID"))
                .assertNext(response -> {
                   var data =  response.getBody()
                           .map(matchEntityModel -> new ModelMapper().map(matchEntityModel, Match.class))
                           .subscribe(match1 -> {
                               assert match1.getGameMinutes() == 90;
                               assert match1.getNumberOfPlayersPerSide() == 5;
                           });
                }).verifyComplete();
    }

    @Test
    void addTeams_toMatch_shouldReturnNewMatch() {
        Match match = Match.builder().numberOfPlayersPerSide(5).build();

        match.setTeams(List.of(new Team(null, "Tigers FC", null)
                ));
        when(matchService.addteamToExistingMatch(any(), any()))
                .thenReturn(Mono.just(match));

        StepVerifier.create(controller.addTeamsToAMatch("matchId", "teamId"))
                .assertNext(monoResponseEntity -> {
                    var data = monoResponseEntity.getBody()
                            .map(matchEntityModel -> new ModelMapper().map(matchEntityModel, Match.class))
                            .subscribe(match1 -> {
                                assert match1.getGameMinutes() == 90;
                                assert match1.getNumberOfPlayersPerSide() == 5;
                                assert match1.getTeams().size() == 1;
                            });
                }).verifyComplete();
    }
}

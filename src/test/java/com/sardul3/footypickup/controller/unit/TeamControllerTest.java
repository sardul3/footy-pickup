package com.sardul3.footypickup.controller.unit;

import com.sardul3.footypickup.controller.TeamController;
import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.PlayerPosition;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    TeamService teamService;

    @InjectMocks
    TeamController teamController;

    @Test
    void addPlayerToTeam_shouldReturnTeamWithNewPlayer() {
        var tiger_players = Set.of(
                new Player("p1", "Angel", "Gyawali", 5, PlayerPosition.CB.name(), false, null),
                new Player("p2", "Rahul", "Gauli", 10, PlayerPosition.CM.name(), false, null)
        );

        var team = new Team("t1", "Tigers FC", "TFC", tiger_players);

        when(teamService.addPlayerToExistingTeam(any(), any()))
                .thenReturn(Mono.just(team));

        StepVerifier.create(teamController.addPlayerToTeam("t1", "p2"))
                .assertNext(teamRes -> {
                   var response =  teamRes.getBody()
                           .map(entity -> new ModelMapper().map(entity, Team.class))
                           .subscribe(team1 -> {
                               assert team1.getName().equalsIgnoreCase("tigers fc");
                               assert team1.getPlayers().size()==2;
                           });
                })
                .verifyComplete();
    }
}

package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.dto.CreateTeamRequest;
import com.sardul3.footypickup.service.TeamService;
import com.sardul3.footypickup.util.ResourceGenerator;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Team>>>> createNewTeam(@Valid @RequestBody CreateTeamRequest createTeamRequest) {
        return teamService.createNewTeam(createTeamRequest)
                .map(teamMono -> ResourceGenerator.getTeamResource(teamMono))
                .map(teamEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(teamEntityModel));
    }

    @PostMapping("/{teamId}/player/{playerId}/add")
    public Mono<ResponseEntity<Mono<EntityModel<Team>>>> addPlayerToTeam(@PathVariable String teamId, @PathVariable String playerId) {
        return teamService.addPlayerToExistingTeam(teamId, playerId)
                .map(teamMono -> ResourceGenerator.getTeamResource(teamMono))
                .map(teamEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(teamEntityModel));
    }

    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<Flux<Player>>> getAllTeams() {
        Flux<Team> teams = teamService.getAllTeams();
//        return ResourceGenerator.getPlayersResource(players)
//                .map(playersEntityModel -> ResponseEntity.status(HttpStatus.OK).body(playersEntityModel));

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(teams);
        return Mono.just(response);
    }
}

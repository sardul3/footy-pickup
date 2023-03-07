package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.Player;
import com.sardul3.footypickup.service.MatchService;
import com.sardul3.footypickup.util.ResourceGenerator;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/{id}/start")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> startMatch(@PathVariable String id) {
        return matchService.startMatch(id)
                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }


    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> createMatchPost(@Valid @RequestBody Match match) {
        return matchService.createNewFootballMatch(match)
                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

    @PostMapping("/{matchId}/team/{teamId}/add")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> addTeamsToAMatch(@PathVariable String matchId, @PathVariable String teamId) {
        return matchService.addteamToExistingMatch(matchId, teamId)
                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

}

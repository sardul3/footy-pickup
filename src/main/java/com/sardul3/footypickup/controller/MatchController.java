package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.ScoreCard;
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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> getMatch(@PathVariable String id) {
        return matchService.retrieveMatch(id)
                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

    @PostMapping("/{id}/start")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> startMatch(@PathVariable String id) {
        return matchService.startMatch(id)
                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

    @PostMapping("/{id}/end")
    public Mono<ResponseEntity<Mono<ScoreCard>>> endMatch(@PathVariable String id) {
        return matchService.endMatch(id)
                .map(scoreCard -> ResponseEntity.status(HttpStatus.OK).body(Mono.just(scoreCard)));
        //                .map(matchMono -> ResourceGenerator.getMatchResource(matchMono))
//                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.OK).body(matchEntityModel));
    }


    @PostMapping
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> createMatchPost(@Valid @RequestBody Match match) {
        return matchService.createNewFootballMatch(match)
                .map(ResourceGenerator::getMatchResource)
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

    @PostMapping("/{matchId}/team/{teamId}/add")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> addTeamsToAMatch(@PathVariable String matchId, @PathVariable String teamId) {
        return matchService.addteamToExistingMatch(matchId, teamId)
                .map(ResourceGenerator::getMatchResource)
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }

    @PostMapping("/{matchId}/team/{teamId}/player/{playerId}/goal")
    public Mono<ResponseEntity<Mono<EntityModel<Match>>>> addGoalToAMatch(@PathVariable String matchId,
                                                                           @PathVariable String teamId,
                                                                           @PathVariable String playerId) {

        return matchService.addGoalToAnOngoingMatch(matchId, teamId, playerId)
                .map(ResourceGenerator::getMatchResource)
                .map(matchEntityModel -> ResponseEntity.status(HttpStatus.CREATED).body(matchEntityModel));
    }



}

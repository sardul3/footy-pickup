package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.repo.MatchRepository;
import com.sardul3.footypickup.repo.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public Mono<Match> createNewFootballMatch(Match match) {
        return matchRepository.save(match);
    }

    public Mono<Match> startMatch(String matchId) {
        return matchRepository.findById(matchId)
                .doOnSuccess(match -> {
                    if(match.getTeams().size()!=2) {
                        log.error("must have 2 teams exact to start the match");
                    }
                })
                .doOnError(err -> {
                    log.error("cannot find the correct match");
                })
                .flatMap((match) -> {
                    match.setGameStarted(true);
                    return matchRepository.save(match);
                });
    }


    public Flux<Match> getAllFootballMatches() {
        return matchRepository.findAll();
    }

    public Mono<Match> addteamToExistingMatch(String matchId, String teamId) {
        return matchRepository.findById(matchId)
                .zipWith(teamRepository.findById(teamId))
                .flatMap(tuple -> {
                    var match = tuple.getT1();
                    var team = tuple.getT2();
                    var matchTeams = match.getTeams();
                    if (matchTeams == null) matchTeams = List.of(team);
                    else matchTeams.add(team);
                    match.setTeams(matchTeams);
                    return matchRepository.save(match);
                });
    }
}

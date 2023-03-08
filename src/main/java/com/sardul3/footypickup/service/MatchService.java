package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.exception.custom.EmptyResourceCollectionException;
import com.sardul3.footypickup.exception.custom.MatchHasInvalidNumberOfTeamsException;
import com.sardul3.footypickup.exception.custom.ResourceNotFoundException;
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
        final int MAX_TEAMS = 2;
        return matchRepository.findById(matchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Match could not be found")))
                .flatMap((match) -> {
                    if(match.getTeams().size()==MAX_TEAMS) {
                        match.setGameStarted(true);
                        return matchRepository.save(match);
                    }
                    return Mono.error(new MatchHasInvalidNumberOfTeamsException("Match must have exactly 2 teams"));
                });
    }


    public Flux<Match> getAllFootballMatches() {
        return matchRepository.findAll()
                .switchIfEmpty(Mono.error(new EmptyResourceCollectionException("No Matches present in DB")));
    }

    public Mono<Match> addteamToExistingMatch(String matchId, String teamId) {
        return matchRepository.findById(matchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Match could not be found")))
                .zipWith(teamRepository.findById(teamId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Team could not be found")))
                )
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

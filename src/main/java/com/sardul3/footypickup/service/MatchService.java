package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Match;
import com.sardul3.footypickup.domain.ScoreCard;
import com.sardul3.footypickup.exception.custom.EmptyResourceCollectionException;
import com.sardul3.footypickup.exception.custom.MatchHasInvalidNumberOfTeamsException;
import com.sardul3.footypickup.exception.custom.ResourceNotFoundException;
import com.sardul3.footypickup.exception.custom.TeamDoesNotHaveMinimumNumberOfPlayersException;
import com.sardul3.footypickup.repo.MatchRepository;
import com.sardul3.footypickup.repo.PlayerRepository;
import com.sardul3.footypickup.repo.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@Slf4j
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public Mono<Match> createNewFootballMatch(Match match) {
        return matchRepository.save(match);
    }

    public Mono<Match> startMatch(String matchId) {
        final int MAX_TEAMS = 2;
        return matchRepository.findById(matchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Match could not be found")))
                .flatMap(match -> {
                    if(match.getTeams().size()==MAX_TEAMS) {
                        int numberOfPlayersInFirstTeam = match.getTeams().stream().toList().get(0).getPlayers().size();
                        int numberOfPlayersInSecondTeam = match.getTeams().stream().toList().get(1).getPlayers().size();
                        if(numberOfPlayersInFirstTeam >= match.getNumberOfPlayersPerSide()
                                && numberOfPlayersInSecondTeam >= match.getNumberOfPlayersPerSide()) {
                            match.setGameStarted(true);
                            return matchRepository.save(match);
                        } else {
                            return Mono.error(new TeamDoesNotHaveMinimumNumberOfPlayersException("need a minimum of " +
                                    match.getNumberOfPlayersPerSide()+ " players in each team"));
                        }

                    }
                    return Mono.error(new MatchHasInvalidNumberOfTeamsException("Match must have exactly 2 teams"));
                });
    }


    public Flux<Match> getAllFootballMatches() {
        return matchRepository.findAll()
                .switchIfEmpty(Mono.error(new EmptyResourceCollectionException("No Matches present in DB")));
    }

    public Mono<Match> addteamToExistingMatch(String matchId, String teamId) {
        final int MAX_LIMIT = 2;
        return matchRepository.findById(matchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("{match.error.not-found}")))
                .zipWith(teamRepository.findById(teamId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("{match.error.not-found}")))
                )
                .flatMap(tuple -> {
                    var match = tuple.getT1();
                    var team = tuple.getT2();
                    var matchTeams = match.getTeams();
                    if (matchTeams == null) matchTeams = Set.of(team);
                    else matchTeams.add(team);
                    if(matchTeams.size() <= MAX_LIMIT) {
                        match.setTeams(matchTeams);
                        return matchRepository.save(match).log();
                    }
                    return Mono.error(new MatchHasInvalidNumberOfTeamsException("Match can only have 2 teams"));
                });
    }

    public Mono<Match> addGoalToAnOngoingMatch(String matchId, String teamId, String playerId) {
        return matchRepository.addGoalEvent(matchId, teamId, playerId);
    }

    public Mono<Match> retrieveMatch(String id) {
        return matchRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Match not found")));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public Mono<ScoreCard> endMatch(String matchId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        return matchRepository.findById(matchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Match could not be found")))
                .flatMap(match -> {
                    match.getGoals().stream().forEach(te -> {
                                var team = teamRepository.findById(te.getId())
                                        .subscribe(t -> {
                                            if(!map.containsKey(t.getName())) {
                                                map.put(t.getName(), 1);
                                            } else {
                                                int currentGoals = map.get(t.getName());
                                                map.put(t.getName(), currentGoals+1);
                                            }
                                            return;
                                        });
                                return;
                            });
                    log.info(String.valueOf(map));
                    return Mono.just(new ScoreCard(map));
                });

    }
//    public Mono<Match> addCardToAnOngoingMatch(String matchId, String teamId, String playerId) {
//
//        return matchRepository.findById(matchId)
//                .switchIfEmpty(Mono.error(new ResourceNotFoundException("{match.error.not-found}")))
//                .zipWith(teamRepository.findById(teamId)
//                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Team could not be found"))))
//                        .zipWith(playerRepository.findById(playerId))
//                        .flatMap(tuple -> {
//                            var player = tuple.getT2();
//                            List<GoalEvent> goals = player.getGoalEvents();
////                            if(goals==null) goals=List.of(new GoalEvent());
////                            else goals.add(new GoalEvent());
//                            player.setGoalEvents(goals);
//                            playerRepository.save(player);
//                            teamRepository.save(tuple.getT1().getT2());
//                            return matchRepository.save(tuple.getT1().getT1());
//                        });
//    }
//
//    public Mono<Match> addGoalToAnOngoingMatch(String matchId, String teamId, String playerId) {
//        var data = matchRepository.findById(matchId)
//                .zipWith(teamRepository.findById(teamId)
//                .zipWith(playerRepository.findById(playerId)));
//        return  data.flatMap(tuple -> {
//            var t  = tuple.getT2().getT1();
//            var p = tuple.getT2().getT2();
//            var m = tuple.getT1();
//
//            var g = GoalEvent.builder().goalBy(p.getFirstName()).build();
//            p.getGoalEvents().add(g);
//            playerRepository.save(p);
//            var savedP = playerRepository.findById(playerId)
//                    .doOnSuccess( ps -> System.out.println(ps));
//            log.info(String.valueOf(savedP));
//
//
//
//
//            t.getPlayers().stream().forEach(te -> {
//                if(te.getId().equalsIgnoreCase(p.getId())) {
//                    te = p;
//                }
//            });
//            m.getTeams().stream().forEach(xy -> {
//                if(xy.getId().equalsIgnoreCase(m.getId())) {
//                    xy.getPlayers().stream().forEach(pp -> {
//                        pp.getGoalEvents().add(g);
//                        playerRepository.save(pp);
//                    });
//                    teamRepository.save(xy);
//                }
//            });
//            playerRepository.save(p);
//            teamRepository.save(t);
//            return matchRepository.save(m);
//        });
//
//
//
//
//
////        return matchRepository.findById(matchId)
////                .switchIfEmpty(Mono.error(new ResourceNotFoundException("{match.error.not-found}")))
////                .zipWith(teamRepository.findById(teamId)
////                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Team could not be found"))))
////                .zipWith(playerRepository.findById(playerId))
////                .flatMap(tuple -> {
////                    var player = tuple.getT2();
////                    // define custom queries
////                    List<GoalEvent> goals = player.getGoalEvents();
////                    var newGoal = GoalEvent.builder().goalBy(player.getFirstName()).build();
////                    if(goals==null) goals=List.of(newGoal);
////                    else goals.add(newGoal);
////                    player.setGoalEvents(goals);
////                    playerRepository.save(player);
////                    var team = tuple.getT1().getT2();
////                    var x = team.getPlayers().stream().filter(p -> p.getId()==playerId)
////                                    .map(player1 -> {
////                                        player1.getGoalEvents().add(newGoal);
////
////                                        return player1;
////                                    }).collect(Collectors.toList()).get(0);
////
////                    teamRepository.save(tuple.getT1().getT2());
////                    var y = tuple.getT1().getT1().getTeams().stream().filter(team1 -> team1.getId()==teamId)
////                            .map(team1 -> team1 = team).toString();
////                    return matchRepository.save(tuple.getT1().getT1());
////                });
//    }
}

package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Match;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomRepo {
    Mono<Match> addGoalEvent(String matchId, String teamId, String playerId);

}

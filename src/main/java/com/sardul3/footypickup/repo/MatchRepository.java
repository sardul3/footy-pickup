package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Match;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends ReactiveMongoRepository<Match, String> {
}

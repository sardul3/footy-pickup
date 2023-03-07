package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends ReactiveMongoRepository<Team, String> {
}

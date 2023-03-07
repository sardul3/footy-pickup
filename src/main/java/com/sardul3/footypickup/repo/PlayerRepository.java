package com.sardul3.footypickup.repo;

import com.sardul3.footypickup.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {
}

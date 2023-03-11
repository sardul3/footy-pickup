package com.sardul3.footypickup.service;

import com.sardul3.footypickup.domain.Team;
import com.sardul3.footypickup.dto.CreateTeamRequest;
import com.sardul3.footypickup.exception.custom.EmptyResourceCollectionException;
import com.sardul3.footypickup.exception.custom.ResourceAlreadyExistsException;
import com.sardul3.footypickup.exception.custom.ResourceNotFoundException;
import com.sardul3.footypickup.repo.PlayerRepository;
import com.sardul3.footypickup.repo.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public Mono<Team> createNewTeam(CreateTeamRequest createTeamRequest) {
        ModelMapper modelMapper = new ModelMapper();
        Team mappedTeam = modelMapper.map(createTeamRequest, Team.class);
        return teamRepository.save(mappedTeam)
                .doOnError(err -> {
                    throw new ResourceAlreadyExistsException(err.getLocalizedMessage());
                });
    }

    public Mono<Team> addPlayerToExistingTeam(String teamId, String playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player could not be found")))
                .zipWith(teamRepository.findById(teamId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Team could not be found")))
                )
                .flatMap(tuple -> {
                    var p = tuple.getT1();
                    var t = tuple.getT2();
                    var ps = t.getPlayers();
                    if(ps == null) ps = Set.of(p);
                    else ps.add(p);
                    t.setPlayers(ps);
                    return teamRepository.save(t);
                });
    }

    public Flux<Team> getAllTeams() {
        return teamRepository.findAll()
                .switchIfEmpty(Mono.error(new EmptyResourceCollectionException("No Teams present in DB")));
    }
}

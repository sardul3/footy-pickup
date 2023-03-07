package com.sardul3.footypickup.domain;

import com.sardul3.footypickup.validation.playerposition.teamplayers.TeamPlayersValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    private String id;

    @NotNull(message="{team.name.notNull.message}")
    private String name;

//    @NotEmpty( message="{team.players.minimum.message}")
//    @TeamPlayersValidator
    private List<Player> players;
}

package com.sardul3.footypickup.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
@Document
@AllArgsConstructor
public class Team {
    @Id
    private String id;

    @Indexed
    @NotNull(message="{team.name.notNull.message}")
    private String name = "team - " + Math.random();

    private String shortHandle = "";

//    @NotEmpty( message="{team.players.minimum.message}")
//    @TeamPlayersValidator
    private Set<Player> players = new HashSet<>();

    public Team() {
        this.players = new HashSet<>();
    }
}

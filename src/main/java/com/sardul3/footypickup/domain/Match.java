package com.sardul3.footypickup.domain;

import com.sardul3.footypickup.domain.events.RedCardEvent;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Document
@Builder
@AllArgsConstructor
public class Match {
//    @MongoId(FieldType.OBJECT_ID)
    @Id
    private String id;

    @Min(value=3, message="{match.players.validRange.message}")
    @Max(value=11, message="{match.players.validRange.message}")
    private int numberOfPlayersPerSide;

    private String location = "Dallas Football Ground";
    private int gameMinutes = 90;
    private boolean gameStarted = false;
    private boolean gameEnded = false;

//    @DocumentReference
    private Set<Team> teams;

    private List<RedCardEvent> events = new ArrayList<>();

    public Match() {
        this.teams = new HashSet<>();
    }

}

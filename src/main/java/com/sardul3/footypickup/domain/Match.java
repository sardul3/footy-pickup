package com.sardul3.footypickup.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Document
@Builder
public class Match {
    @Id
    private String id;

    @Min(value=3, message="{match.players.validRange.message}")
    @Max(value=11, message="{match.players.validRange.message}")
    private int numberOfPlayersPerSide;

    private String location = "Dallas Football Ground";
    private int gameMinutes = 90;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private List<Team> teams = new ArrayList<>();
}

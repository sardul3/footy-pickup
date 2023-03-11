package com.sardul3.footypickup.domain;


import com.sardul3.footypickup.domain.events.GoalEvent;
import com.sardul3.footypickup.domain.events.RedCardEvent;
import com.sardul3.footypickup.validation.playerposition.PlayerPositionValidator;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
    private String id;

    @NotNull(message="{player.firstName.notNull.message}")
    @Size(min=1, message="{player.firstName.notNull.message}")
    private String firstName;

    @NotNull(message="{player.lastName.notNull.message}")
    @Size(min=1, message="{player.lastName.notNull.message}")
    @Indexed
    private String lastName;

    @Min(value=1, message="{player.shirtNumber.validRange.message}")
    @Max(value=99, message="{player.shirtNumber.validRange.message}")
    private int shirtNumber;

    @PlayerPositionValidator(enumClass = PlayerPosition.class, message = "{player.position.validRole.message}")
    @Indexed
    private String playerPosition;

    @Indexed
    private boolean isCaptain;

    private List<RedCardEvent> redCardEvents = new ArrayList<>();

    public Player(String id, String firstName, String lastName, int shirtNumber, String playerPosition, boolean isCaptain, List<RedCardEvent> redCardEvents) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shirtNumber = shirtNumber;
        this.playerPosition = playerPosition;
        this.isCaptain = isCaptain;
        this.redCardEvents = redCardEvents;
    }

    private List<GoalEvent> goalEvents = new ArrayList<>();
}

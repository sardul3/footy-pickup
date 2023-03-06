package com.sardul3.footypickup.domain;


import com.sardul3.footypickup.config.PlayerPositionValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Document
@AllArgsConstructor
public class Player {
    @Id
    private String id;

    @NotNull(message="{player.firstName.notNull.message}")
    @Size(min=1, message="{player.firstName.notNull.message}")
    private String firstName;

    @NotNull(message="{player.lastName.notNull.message}")
    @Size(min=1, message="{player.lastName.notNull.message}")
    private String lastName;

    @Min(value=1, message="{player.shirtNumber.validRange.message}")
    @Max(value=99, message="{player.shirtNumber.validRange.message}")
    private int shirtNumber;

    @PlayerPositionValidator(enumClass = PlayerPosition.class, message = "{player.position.validRole.message}")
    private String playerPosition;

    private boolean isCaptain;
}

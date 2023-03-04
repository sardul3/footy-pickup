package com.sardul3.footypickup.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Document
@AllArgsConstructor
public class Player {
    @Id
    private String id;

    @NotNull(message="first name cannot be empty")
    @Size(min=1, message="first name must have at least one character")
    private String firstName;

    @NotNull(message="last name cannot be empty")
    @Size(min=1, message="last name must have at least one character")
    private String lastName;

    @Min(value=1, message="shirt number must be within 1 - 99")
    @Max(value=99, message="shirt number must be within 1 - 99")
    private int shirtNumber;
    private PlayerPosition playerPosition;
    private boolean isCaptain;
}

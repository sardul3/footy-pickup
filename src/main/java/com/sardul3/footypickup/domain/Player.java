package com.sardul3.footypickup.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document
@AllArgsConstructor
public class Player {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private int shirtNumber;
    private PlayerPosition playerPosition;
    private boolean isCaptain;
}

package com.sardul3.footypickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {
    @NotNull(message="{team.name.notNull.message}")
    private String name;
}

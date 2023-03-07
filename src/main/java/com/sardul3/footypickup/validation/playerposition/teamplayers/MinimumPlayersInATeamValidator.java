package com.sardul3.footypickup.validation.playerposition.teamplayers;

import com.sardul3.footypickup.domain.Player;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.List;

public class MinimumPlayersInATeamValidator implements ConstraintValidator<MinimumPlayersInATeamValidator, List<Player>>, Annotation {
    @Override
    public boolean isValid(List<Player> players, ConstraintValidatorContext constraintValidatorContext) {
        return players.size()>=3;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}

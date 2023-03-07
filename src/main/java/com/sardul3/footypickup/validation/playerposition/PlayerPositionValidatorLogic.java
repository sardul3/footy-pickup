package com.sardul3.footypickup.validation.playerposition;

import com.sardul3.footypickup.validation.playerposition.PlayerPositionValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerPositionValidatorLogic implements ConstraintValidator<PlayerPositionValidator, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(PlayerPositionValidator annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence  playerPosition, ConstraintValidatorContext constraintValidatorContext) {
        if (playerPosition == null) {
            return false;
        }
        return acceptedValues.contains(playerPosition.toString());
    }
}

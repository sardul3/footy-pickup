package com.sardul3.footypickup.validation.playerposition.teamplayers;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD,PARAMETER})
@Constraint(validatedBy = MinimumPlayersInATeamValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TeamPlayersValidator {
    String message() default "The new team must contain at least 3 players";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

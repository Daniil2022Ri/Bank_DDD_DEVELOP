package com.bank.authorization.utils.validation;

import com.bank.authorization.utils.ApplicationConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bank.authorization.utils.ApplicationConstants.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorUniqueProfile.class)
public @interface ValidUniqueProfile {
    String message() default EXISTS_PROFILE_ID;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

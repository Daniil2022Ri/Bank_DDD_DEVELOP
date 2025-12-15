package com.bank.authorization.utils.validation;

import com.bank.authorization.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatorUniqueProfile implements ConstraintValidator<ValidUniqueProfile, Long> {
     private final UserRepository userRepository;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        return !userRepository.existsByProfileId(aLong);
    }
}

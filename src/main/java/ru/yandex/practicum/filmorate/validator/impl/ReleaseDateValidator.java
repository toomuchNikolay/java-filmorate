package ru.yandex.practicum.filmorate.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValid;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValid, LocalDate> {

    private static final LocalDate MIN_DATE_FOR_RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (localDate == null) return true;
        return !localDate.isBefore(MIN_DATE_FOR_RELEASE);
    }
}

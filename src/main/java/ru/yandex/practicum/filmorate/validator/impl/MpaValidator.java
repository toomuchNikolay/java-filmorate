package ru.yandex.practicum.filmorate.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.validator.MpaValid;

@Component
public class MpaValidator implements ConstraintValidator<MpaValid, MpaDto> {
    @Autowired
    private MpaService service;

    @Override
    public boolean isValid(MpaDto mpa, ConstraintValidatorContext context) {
        if (mpa == null) return true;
        try {
            service.getMpaById(mpa.getId());
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}

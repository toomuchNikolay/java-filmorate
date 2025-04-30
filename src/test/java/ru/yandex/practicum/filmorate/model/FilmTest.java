package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {
    @Autowired
    private Validator validator;
    Set<ConstraintViolation<Film>> postViolations;
    Set<ConstraintViolation<Film>> putViolations;

    private Film postFilm;
    private Film putFilm;

    @BeforeEach
    void setUp() {
        postViolations = new HashSet<>();
        putViolations = new HashSet<>();

        postFilm = Film.builder()
                .name("First film")
                .description("description first film")
                .releaseDate(LocalDate.of(2022, 1, 11))
                .duration(150)
                .build();
        putFilm = Film.builder()
                .id(22L)
                .name("Second film")
                .description("description second film")
                .releaseDate(LocalDate.of(2011, 2, 22))
                .duration(120)
                .build();
    }

    @Test
    void checkNullAnnotationFieldId() {
        postViolations = validator.validate(putFilm, ValidationGroups.PostValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка id при добавлении фильма");
    }

    @Test
    void checkNotNullAnnotationFieldId() {
        putViolations = validator.validate(postFilm, ValidationGroups.PutValidationGroup.class);

        assertFalse(putViolations.isEmpty(), "Не прошла проверка id при обновлении фильма");
    }

    @Test
    void checkNotBlankAnnotationFieldName() {
        postFilm.setName("");
        putFilm.setName(" ");
        postViolations = validator.validate(postFilm, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putFilm, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка на пустое название при добавлении фильма");
        assertFalse(putViolations.isEmpty(), "Не прошла проверка на пустое название при обновлении фильма");
    }

    @Test
    void checkSizeAnnotationFieldDescription() {
        postFilm.setDescription("bad description".repeat(14));
        putFilm.setDescription("bad".repeat(67));
        postViolations = validator.validate(postFilm, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putFilm, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(),
                "Не прошла проверка на количество символов в описании при добавлении фильма");
        assertFalse(putViolations.isEmpty(),
                "Не прошла проверка на количество символов в описании при обновлении фильма");
    }

    @Test
    void checkReleaseDateValidAnnotationFieldReleaseDate() {
        postFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        putFilm.setReleaseDate(LocalDate.of(1894, 12, 28));
        postViolations = validator.validate(postFilm, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putFilm, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка на минимальную дату при добавлении фильма");
        assertFalse(putViolations.isEmpty(), "Не прошла проверка на минимальную дату при обновлении фильма");
    }

    @Test
    void checkPositiveAnnotationFieldDuration() {
        postFilm.setDuration(0);
        putFilm.setDuration(-11111111);
        postViolations = validator.validate(postFilm, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putFilm, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка на продолжительность при добавлении фильма");
        assertFalse(putViolations.isEmpty(), "Не прошла проверка на продолжительность при обновлении фильма");
    }
}

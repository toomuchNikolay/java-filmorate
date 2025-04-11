package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("test name film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 10, 20));
        film.setDuration(150);
    }

    @Test
    void shouldNotViolationsWhenFieldCorrect() {
        Set<ConstraintViolation<Film>> violations = validatorFactoryBean.validate(film);

        assertTrue(violations.isEmpty(), "Ошибка в валидации полей Film");
    }

    @Test
    void checkAnnotationFieldName() {
        film.setName(" ");
        Set<ConstraintViolation<Film>> violations = validatorFactoryBean.validate(film);

        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage(),
                "Не прошла проверка на пустое название фильма");
    }

    @Test
    void checkAnnotationFieldDescription() {
        film.setDescription("test".repeat(50));
        Set<ConstraintViolation<Film>> violations = validatorFactoryBean.validate(film);

        assertEquals(200, film.getDescription().length(), "Ошибка в размере описания фильма");
        assertTrue(violations.isEmpty(), "Не прошла проверка на максимально допустимый размер описания");

        film.setDescription("test".repeat(51));
        violations = validatorFactoryBean.validate(film);

        assertEquals("Описание не может превышать 200 символов", violations.iterator().next().getMessage(),
                "Не прошла проверка на превышение размера описания фильма");
    }

    @Test
    void checkAnnotationFieldReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        Set<ConstraintViolation<Film>> violations = validatorFactoryBean.validate(film);

        assertTrue(violations.isEmpty(), "Не прошла проверка на предельно допустимую дату");

        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        violations = validatorFactoryBean.validate(film);

        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года",
                violations.iterator().next().getMessage(), "Не прошла проверка на превышение допустимой даты");
    }

    @Test
    void checkAnnotationFieldDuration() {
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validatorFactoryBean.validate(film);

        assertTrue(violations.isEmpty(), "Не прошла проверка на предельно допустимую продолжительность");

        film.setDuration(0);
        violations = validatorFactoryBean.validate(film);

        assertEquals("Продолжительность фильма должна быть положительным числом",
                violations.iterator().next().getMessage(), "Не прошла проверка на продолжительность");
    }
}

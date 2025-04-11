package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;

    private Film film;

    @BeforeEach
    void setUp() {
        controller.getAllFilms().clear();
        film = new Film();
        film.setName("Test Name Film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 10, 20));
        film.setDuration(Duration.ofMinutes(150));
    }

    @Test
    void checkMethodAdd() {
        controller.add(film);

        assertNotNull(film.getId(), "При добавлении фильма не присвоился id");
        assertTrue(controller.getAllFilms().contains(film), "Фильм не добавился в БД");

        Film duplicate = new Film();
        duplicate.setName(film.getName().toLowerCase());
        duplicate.setDescription("duplicate description");
        duplicate.setReleaseDate(LocalDate.of(2022, 12, 12));
        duplicate.setDuration(Duration.ofMinutes(120));

        DuplicatedDataException e = assertThrows(DuplicatedDataException.class, () -> controller.add(duplicate));
        assertEquals("Фильм с указанным названием уже добавлен", e.getMessage(),
                "Не сработало исключение при добавлении фильма с аналогичным названием");
    }

    @Test
    void checkMethodUpdate() {
        controller.add(film);

        assertEquals(1, controller.getAllFilms().size(), "Не добавился фильм в БД");

        Film updateFilm = new Film();
        updateFilm.setName(film.getName());
        updateFilm.setDescription("new description");
        updateFilm.setReleaseDate(film.getReleaseDate());
        updateFilm.setDuration(film.getDuration());

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.update(updateFilm));
        assertEquals("id должен быть указан", exception.getMessage(),
                "Не сработало исключение при обновлении фильма без указания id");

        updateFilm.setId(2L);

        NotFoundException e = assertThrows(NotFoundException.class, () -> controller.update(updateFilm));
        assertEquals("Фильм с id 2 не найден", e.getMessage(),
                "Не сработало исключение при обновлении фильма с указанием несуществующего id");

        updateFilm.setId(film.getId());
        controller.update(updateFilm);

        assertEquals(1, controller.getAllFilms().size(), "Изменилось количество в БД");

        assertFalse(controller.getAllFilms().stream()
                        .anyMatch(f -> f.getDescription().equalsIgnoreCase("description")),
                "В БД есть фильм со старым описанием");
        assertTrue(controller.getAllFilms().stream()
                        .anyMatch(f -> f.getDescription().equalsIgnoreCase("new description")),
                "В БД не обновилось описание фильма");
    }
}
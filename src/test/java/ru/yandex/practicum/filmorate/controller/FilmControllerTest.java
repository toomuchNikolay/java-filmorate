package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;

    private Film film;

    @BeforeEach
    void setUp() {
        controller.getFilms().clear();
        controller.getAddFilms().clear();
        film = new Film();
        film.setName("Test Name Film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 10, 20));
        film.setDuration(150);
    }

    @Test
    void checkMethodGetAllFilms() {
        controller.add(film);

        Film yetFilm = new Film();
        yetFilm.setName("Test Another Name Film");
        yetFilm.setDescription("");
        yetFilm.setReleaseDate(LocalDate.of(2022, 2, 22));
        yetFilm.setDuration(90);
        controller.add(yetFilm);

        assertEquals(2, controller.getAllFilms().size(), "Ошибка в количестве добавленных фильмов");
        assertTrue(controller.getAllFilms().contains(film), "При получении списка фильмов отсутствует film");
        assertTrue(controller.getAllFilms().contains(yetFilm), "При получении списка фильмов отсутствует yetFilm");
    }

    @Test
    void checkMethodAdd() {
        controller.add(film);

        assertNotNull(film.getId(), "При добавлении фильма не присвоился id");
        assertTrue(controller.getAllFilms().contains(film), "Фильм не добавился в БД");
        assertTrue(controller.getAddFilms().contains(film.getName().toLowerCase().trim()),
                "Не добавилось в хранилище добавленных указанное название фильма");

        Film duplicate = new Film();
        duplicate.setName(film.getName().toLowerCase());
        duplicate.setDescription("duplicate description");
        duplicate.setReleaseDate(film.getReleaseDate());
        duplicate.setDuration(120);

        DuplicatedDataException e = assertThrows(DuplicatedDataException.class, () -> controller.add(duplicate));
        assertEquals("Указанный фильм уже добавлен", e.getMessage(),
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

    @Test
    void checkCurrentStatusSetName() {
        controller.add(film);

        Film yetFilm = new Film();
        yetFilm.setName("Test Another Name Film");
        yetFilm.setDescription("");
        yetFilm.setReleaseDate(LocalDate.of(2022, 2, 22));
        yetFilm.setDuration(90);
        controller.add(yetFilm);

        assertEquals(2, controller.getAddFilms().size(),
                "Ошибка в количестве добавленных названий фильмов в хранилище");
        assertTrue(controller.getAddFilms().contains("test name film"),
                "В хранилище добавленных названий фильмов отсутствует название фильма film");
        assertTrue(controller.getAddFilms().contains("test another name film"),
                "В хранилище добавленных названий фильмов отсутствует название фильма yetFilm");

        Film updateFilmTrue = new Film();
        updateFilmTrue.setId(film.getId());
        updateFilmTrue.setName("Update name film");
        updateFilmTrue.setDescription("description update film");
        updateFilmTrue.setReleaseDate(film.getReleaseDate());
        updateFilmTrue.setDuration(film.getDuration());
        controller.update(updateFilmTrue);

        assertEquals(2, controller.getAddFilms().size(),
                "Ошибка в количестве добавленных названий фильмов в хранилище");
        assertTrue(controller.getAddFilms().contains("update name film"),
                "В хранилище добавленных названий фильмов не добавилось новое название film");
        assertFalse(controller.getAddFilms().contains("test name film"),
                "В хранилище добавленных названий фильмов не удалилось старое название film");

        Film updateFilmFalse = new Film();
        updateFilmFalse.setId(yetFilm.getId());
        updateFilmFalse.setName(" update NAME film ");
        updateFilmFalse.setDescription("");
        updateFilmFalse.setReleaseDate(yetFilm.getReleaseDate());
        updateFilmFalse.setDuration(yetFilm.getDuration());

        DuplicatedDataException e = assertThrows(DuplicatedDataException.class, () -> controller.update(updateFilmFalse));
        assertEquals("Указанный фильм уже добавлен", e.getMessage(),
                "Не сработало исключение при обновлении названия фильма, которое уже есть в хранилище используемых");
        assertTrue(controller.getAllFilms().contains(yetFilm),
                "При обновлении фильма и указании существующего названия фильма данные обновились");
        assertTrue(controller.getAddFilms().contains("test another name film"),
                "В хранилище добавленных фильмов изменилось название при обновлении фильма");
    }
}
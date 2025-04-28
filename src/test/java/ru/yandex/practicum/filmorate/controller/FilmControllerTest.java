package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;
    @Autowired
    private FilmStorage storage;

    private Film film;
    private final User user = User.builder()
            .email("testadress@email.com")
            .login("MyLogin")
            .name("Name")
            .birthday(LocalDate.of(2000, 10, 10))
            .build();

    @BeforeEach
    void setUp() {
        ((InMemoryFilmStorage) storage).getFilms().clear();
        ((InMemoryFilmStorage) storage).getAddedFilms().clear();
        ((InMemoryFilmStorage) storage).getFilmsRating().clear();
        film = Film.builder()
                .name("Test name film")
                .description("description film")
                .releaseDate(LocalDate.of(2022, 1, 11))
                .duration(150)
                .build();
    }

    @Test
    void checkMethodAddFilm() {
        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "При успешном добавлении фильма вернулся не 201 статус");
    }

    @Test
    void checkMethodUpdateFilm() {
        filmController.addFilm(film);
        Film updatedFilm = Film.builder()
                .id(film.getId())
                .name("New name film")
                .description("new description")
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "При успешном обновлении фильма вернулся на 200 статус");
    }

    @Test
    void checkMethodAddLikeAndRemoveLike() {
        filmController.addFilm(film);
        userController.addUser(user);
        ResponseEntity<Void> responseAddLike = filmController.addLike(film.getId(), user.getId());
        ResponseEntity<Void> responseRemoveLike = filmController.removeLike(film.getId(), user.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseAddLike.getStatusCode(),
                "При успешном добавлении лайка фильму вернулся не 204 статус");
        assertEquals(HttpStatus.NO_CONTENT, responseRemoveLike.getStatusCode(),
                "При успешном удалении лайка у фильма вернулся не 204 статус");
    }

    @Test
    void checkGetMethods() {
        Film yetFilm = Film.builder()
                .name("Another film")
                .description("empty")
                .releaseDate(LocalDate.of(2000, 10, 1))
                .duration(90)
                .build();
        filmController.addFilm(film);
        filmController.addFilm(yetFilm);
        ResponseEntity<Collection<Film>> responseGetAllFilms = filmController.getAllFilms();
        ResponseEntity<Collection<Film>> responseGetPopularFilms = filmController.getPopularFilms(2);

        assertEquals(HttpStatus.OK, responseGetAllFilms.getStatusCode(),
                "При успешном запросе списка всех фильмов вернулся не 200 статус");
        assertEquals(HttpStatus.OK, responseGetPopularFilms.getStatusCode(),
                "При успешном запросе списка популярных фильмов вернулся не 200 статус");
    }
}

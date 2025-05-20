package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class FilmControllerTest {
    private final FilmController filmController;

    @Test
    void addFilm() {
        NewFilmRequest request = NewFilmRequest.builder()
                .name("test film title")
                .description("test film description")
                .releaseDate(LocalDate.of(2000, 10, 1))
                .duration(100)
                .mpa(MpaDto.builder().id(3L).build())
                .genres(List.of(GenreDto.builder().id(2L).build()))
                .build();
        FilmDto film = filmController.addFilm(request);
        FilmDto findFilm = filmController.getFilmById(film.getId());

        assertThat(film.getId()).isNotNull();
        assertThat(findFilm).isEqualTo(film);
    }

    @Test
    void updateFilm() {
        UpdateFilmRequest request = UpdateFilmRequest.builder()
                .id(1L)
                .description("new description")
                .releaseDate(LocalDate.of(2022, 2, 22))
                .build();
        FilmDto film = filmController.updateFilm(request);
        Collection<FilmDto> collection = filmController.getAllFilms();

        assertThat(collection)
                .extracting(FilmDto::getDescription)
                .contains(film.getDescription());
    }

    @Test
    void addAndRemoveLike() {
        filmController.addLike(1L, 2L);
        filmController.addLike(1L, 3L);
        filmController.addLike(1L, 5L);
        filmController.addLike(2L, 1L);
        filmController.addLike(3L, 2L);
        filmController.addLike(3L, 4L);
        Collection<FilmDto> collection = filmController.getPopularFilms(5);

        assertThat(collection)
                .hasSize(3)
                .extracting(FilmDto::getId)
                .containsExactly(1L, 3L, 2L);

        filmController.removeLike(2L, 1L);

        assertThat(filmController.getFilmLikes(2L)).isEmpty();
    }
}
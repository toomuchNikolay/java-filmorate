package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbFilmStorage.class, FilmRowMapper.class})
class DbFilmStorageTest {
    private final DbFilmStorage storage;

    private Film createFilm() {
        return Film.builder()
                .name("title")
                .description("description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(100)
                .mpa(Mpa.builder().mpaId(3L).build())
                .genres(Set.of(Genre.builder().genreId(1L).build(), Genre.builder().genreId(2L).build()))
                .build();
    }

    @Test
    void addFilm() {
        Film added = storage.addFilm(createFilm());

        assertThat(added.getFilmId()).isNotNull();
        assertThat(added.getName()).isEqualTo("title");
    }

    @Test
    void updateFilm() {
        Film added = storage.addFilm(createFilm());
        added.setDescription("qwerty");
        added.setDuration(99);
        Film updated = storage.updateFilm(added);

        assertThat(updated.getDescription()).isEqualTo("qwerty");
        assertThat(updated.getDuration()).isEqualTo(99);
    }

    @Test
    void findAllFilms() {
        Film first = storage.addFilm(createFilm());
        Collection<Film> collection = storage.findAllFilms();

        assertThat(collection)
                .hasSize(6)
                .extracting(Film::getName)
                .contains(first.getName());
    }

    @Test
    void findFilmById() {
        Film added = storage.addFilm(createFilm());
        Long id = added.getFilmId();
        Optional<Film> findFilm = storage.findFilmById(id);

        assertThat(findFilm)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f.getFilmId()).isEqualTo(id));
    }
}
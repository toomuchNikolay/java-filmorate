package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmGenreRowMapper;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbFilmGenreStorage.class, FilmGenreRowMapper.class})
class DbFilmGenreStorageTest {
    private final DbFilmGenreStorage storage;

    @Test
    void addFilmGenre() {
        storage.addFilmGenre(1L, List.of(Genre.builder().genreId(3L).build()));
        Collection<Long> collection = storage.findGenresFilm(1L);

        assertThat(collection)
                .hasSize(1)
                .contains(3L);
    }

    @Test
    void removeFilmGenre() {
        storage.addFilmGenre(1L, List.of(Genre.builder().genreId(2L).build(), Genre.builder().genreId(4L).build()));
        Collection<Long> collection = storage.findGenresFilm(1L);

        assertThat(collection)
                .hasSize(2);

        storage.removeFilmGenres(1L);
        collection = storage.findGenresFilm(1L);

        assertThat(collection).isEmpty();
    }
}
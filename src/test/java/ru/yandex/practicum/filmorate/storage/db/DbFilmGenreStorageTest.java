package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmGenreRowMapper;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbFilmGenreStorage.class, FilmGenreRowMapper.class})
class DbFilmGenreStorageTest {
    private final DbFilmGenreStorage storage;

    @Test
    void addFilmGenre() {
        storage.addFilmGenre(1L, 3L);
        Collection<Long> collection = storage.findGenresFilm(1L);

        assertThat(collection)
                .hasSize(1)
                .contains(3L);
    }

    @Test
    void removeFilmGenre() {
        storage.addFilmGenre(1L, 2L);
        storage.addFilmGenre(1L, 4L);
        Collection<Long> collection = storage.findGenresFilm(1L);

        assertThat(collection)
                .hasSize(2);

        storage.removeFilmGenres(1L);
        collection = storage.findGenresFilm(1L);

        assertThat(collection).isEmpty();
    }
}
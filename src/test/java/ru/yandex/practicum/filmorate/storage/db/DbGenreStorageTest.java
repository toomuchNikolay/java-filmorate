package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbGenreStorage.class, GenreRowMapper.class})
class DbGenreStorageTest {
    private final DbGenreStorage storage;

    @Test
    void findAllGenre() {
        Collection<Genre> collection = storage.findAllGenres();

        assertThat(collection)
                .hasSize(6)
                .extracting(Genre::getName)
                .contains("Боевик", "Триллер", "Комедия");
    }

    @Test
    void findGenreById() {
        Optional<Genre> findGenre = storage.findGenreById(3L);

        assertThat(findGenre)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getName()).isEqualTo("Мультфильм"));
    }
}
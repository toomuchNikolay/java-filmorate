package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Repository
public class InMemoryGenreStorage implements GenreStorage {
    private final Map<Long, Genre> genres = Map.of(
            1L, new Genre(1L, "Комедия"),
            2L, new Genre(2L, "Драма"),
            3L, new Genre(3L, "Мультфильм"),
            4L, new Genre(4L, "Триллер"),
            5L, new Genre(5L, "Документальный"),
            6L, new Genre(6L, "Боевик")
    );

    @Override
    public Collection<Genre> findAllGenres() {
        return genres.values();
    }

    @Override
    public Optional<Genre> findGenreById(Long genreId) {
        return Optional.ofNullable(genres.get(genreId));
    }
}

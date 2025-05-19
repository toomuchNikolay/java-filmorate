package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DbGenreStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL = "SELECT * FROM genres ORDER BY genre_id";
    private static final String FIND_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

    public DbGenreStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> findAllGenres() {
        return getMany(FIND_ALL);
    }

    @Override
    public Optional<Genre> findGenreById(Long genreId) {
        return getOne(FIND_BY_ID, genreId);
    }
}

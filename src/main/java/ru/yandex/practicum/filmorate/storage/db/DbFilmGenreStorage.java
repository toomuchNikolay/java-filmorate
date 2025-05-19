package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.Collection;

@Repository
@Primary
public class DbFilmGenreStorage extends BaseDbStorage<FilmGenre> implements FilmGenreStorage {
    private static final String INSERT = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String FIND_GENRES = "SELECT genre_id FROM film_genres WHERE film_id = ?";

    public DbFilmGenreStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFilmGenre(Long filmId, Long genreId) {
        insertWithoutKey(INSERT,
                filmId,
                genreId
                );
    }

    @Override
    public void removeFilmGenres(Long filmId) {
        delete(DELETE,
                filmId
        );
    }

    @Override
    public Collection<Long> findGenresFilm(Long filmId) {
        return jdbc.queryForList(FIND_GENRES, Long.class, filmId);
    }
}

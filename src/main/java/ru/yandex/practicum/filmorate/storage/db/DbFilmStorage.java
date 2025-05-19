package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Repository
@Primary
public class DbFilmStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String INSERT = "INSERT INTO films(title, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE films " +
            "SET title = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String FIND_ALL = "SELECT f.*, m.name, STRING_AGG(g.name, ', ') AS genres FROM films f " +
            "LEFT JOIN MPA_RATINGS m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID " +
            "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID GROUP BY f.FILM_ID";
    private static final String FIND_BY_ID = "SELECT f.*, m.name, STRING_AGG(g.name, ', ') AS genres FROM films f " +
            "LEFT JOIN MPA_RATINGS m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID " +
            "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID WHERE f.FILM_ID = ? GROUP BY f.FILM_ID";

    public DbFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film addFilm(Film film) {
        Long id = insert(
                INSERT,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getMpaId()
        );
        film.setFilmId(id);
//        film.getGenres().stream()
//                .map(Genre::getGenreId)
//                .toList();
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getMpaId(),
                film.getFilmId()
        );
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return getMany(FIND_ALL);
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        return getOne(FIND_BY_ID, filmId);
    }
}

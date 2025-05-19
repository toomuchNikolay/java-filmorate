package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface FilmGenreStorage {
    void addFilmGenre(Long filmId, Long genreId);

    void removeFilmGenres(Long filmId);

    Collection<Long> findGenresFilm(Long filmId);
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface FilmGenreStorage {
    void addFilmGenre(Long filmId, Collection<Genre> genres);

    void removeFilmGenres(Long filmId);

    Collection<Long> findGenresFilm(Long filmId);
}

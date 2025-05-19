package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.*;

@Repository
public class InMemoryFilmGenreStorage implements FilmGenreStorage {
    private final Set<FilmGenre> filmGenres = new HashSet<>();

    @Override
    public void addFilmGenre(Long filmId, Long genreId) {
        FilmGenre filmGenre = FilmGenre.builder()
                .filmId(filmId)
                .genreId(genreId)
                .build();
        filmGenres.add(filmGenre);
    }

    @Override
    public void removeFilmGenres(Long filmId) {
        filmGenres.removeIf(fg -> fg.getFilmId().equals(filmId));
    }

    @Override
    public Collection<Long> findGenresFilm(Long filmId) {
        return filmGenres.stream()
                .filter(fg -> fg.getFilmId().equals(filmId))
                .map(FilmGenre::getGenreId)
                .toList();
    }
}

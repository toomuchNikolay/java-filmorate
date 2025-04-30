package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> addedFilms = new HashSet<>();
    private final Set<Film> filmsRating = new TreeSet<>(Comparator.comparingInt((Film f) -> f.getLikes().size())
            .reversed()
            .thenComparing(Film::getName));

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        addedFilms.add(film.getName().toLowerCase().trim());
        filmsRating.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);
        String oldFilmName = films.get(film.getId()).getName().toLowerCase().trim();
        String newFilmName = film.getName().toLowerCase().trim();
        if (!oldFilmName.equals(newFilmName)) {
            addedFilms.remove(oldFilmName);
            addedFilms.add(newFilmName);
        }
        filmsRating.remove(films.get(film.getId()));
        filmsRating.add(film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> getRatingFilms(Integer count) {
        return filmsRating.stream()
                .limit(count)
                .toList();
    }

    @Override
    public boolean isUsedName(String name) {
        return addedFilms.contains(name.toLowerCase().trim());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

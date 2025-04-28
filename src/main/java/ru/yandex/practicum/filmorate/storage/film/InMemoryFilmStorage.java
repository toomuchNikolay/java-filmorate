package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@Getter
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> addedFilms = new HashSet<>();
    private final Set<Film> filmsRating = new TreeSet<>(Comparator.comparingInt((Film f) -> f.getLikes().size())
            .reversed()
            .thenComparing(Film::getName));

    @Override
    public Film addFilm(Film film) {
        log.debug("Добавление в хранилище фильма {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} успешно добавлен под id {}", film, film.getId());
        addedFilms.add(film.getName().toLowerCase().trim());
        log.debug("В список уникальных названий фильмов добавлено {}", film.getName().toLowerCase().trim());
        filmsRating.add(film);
        log.debug("В рейтинговый список добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Обновление в хранилище фильма {} с id {}", film, film.getId());
        films.replace(film.getId(), film);
        log.info("Фильм {} в хранилище успешно обновлен", film);
        String oldFilmName = films.get(film.getId()).getName().toLowerCase().trim();
        String newFilmName = film.getName().toLowerCase().trim();
        if (!oldFilmName.equals(newFilmName)) {
            addedFilms.remove(oldFilmName);
            log.debug("Из списка уникальный названий фильмов удалено {}", oldFilmName);
            addedFilms.add(newFilmName);
            log.debug("В список уникальных названий фильмов добавлено {}", newFilmName);
        }
        Set<Long> oldFilmLikes = films.get(film.getId()).getLikes();
        Set<Long> newFilmLikes = film.getLikes();
        if (oldFilmLikes.equals(newFilmLikes)) {
            filmsRating.remove(films.get(film.getId()));
            log.debug("Из рейтингового списка удален фильм {}", films.get(film.getId()));
            filmsRating.add(film);
            log.debug("В рейтинговый список добавлен фильм {}", film);
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> result = films.values();
        log.info("Список всех фильмов успешно получен");
        return result;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> getRatingFilms(Integer count) {
        Collection<Film> result = filmsRating.stream()
                .limit(count)
                .toList();
        log.info("Рейтинговый список фильмов успешно получен");
        return result;
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

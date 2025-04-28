package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        log.debug("Попытка добавить фильм {}", film);
        if (filmStorage.isUsedName(film.getName())) {
            log.warn("Попытка добавить дубликат фильма {}", film.getName());
            throw new DuplicatedDataException("Указанный фильм уже добавлен");
        }
        if (!film.getLikes().isEmpty()) {
            log.warn("Попытка добавить фильм {} с заполненным списком лайков", film);
            throw new ValidationException("У нового фильма список лайков должен быть пустой");
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.debug("Попытка обновить фильм {}", film);
        Film oldFilm = filmStorage.findFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с id " + film.getId() + " не найден"));
        String nameOld = oldFilm.getName().toLowerCase().trim();
        String nameNew = film.getName().toLowerCase().trim();
        if (!nameNew.equals(nameOld)) {
            if (filmStorage.isUsedName(nameNew)) {
                log.warn("Попытка добавить дубликат фильма {}", film.getName());
                throw new DuplicatedDataException("Фильм с указанным названием уже добавлен");
            }
        }
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        log.debug("Попытка получить список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", id);
                    return new NotFoundException("Фильм с id " + id + " не найден");
                });
    }

    public void addLike(Long id, Long userId) {
        log.debug("Попытка добавить лайк пользователем {} фильму {}", userId, id);
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", id);
                    return new NotFoundException("Фильм с id " + id + " не найден");
                });
        User user = userService.getUserById(userId);
        if (film.getLikes().contains(userId)) {
            log.warn("Попытка пользователя {} повторно добавить лайк фильму {}", userId, id);
            throw new DuplicatedDataException("Пользователь " + userId + " уже поставил лайк фильму " + id);
        }
        film.getLikes().add(user.getId());
        filmStorage.updateFilm(film);
        log.info("Лайк пользователя {} фильму {} успешно добавлен", userId, id);
    }

    public void removeLike(Long id, Long userId) {
        log.debug("Попытка удалить лайк пользователем {} у фильма {}", userId, id);
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", id);
                    return new NotFoundException("Фильм с id " + id + " не найден");
                });
        User user = userService.getUserById(userId);
        if (!film.getLikes().contains(userId)) {
            log.warn("Попытка пользователя {} удалить отсутствующий лайк фильму {}", userId, id);
            throw new NotFoundException("У фильма " + id + " отсутствует лайк от пользователя " + userId);
        }
        film.getLikes().remove(user.getId());
        filmStorage.updateFilm(film);
        log.info("Лайк пользователя {} у фильма {} успешно удален", userId, id);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        log.debug("Попытка получить рейтинговый список фильмов");
        return filmStorage.getRatingFilms(count);
    }
}

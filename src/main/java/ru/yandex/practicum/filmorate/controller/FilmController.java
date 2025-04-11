package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Отправлен запрос на получение списка фильмов");
        Collection<Film> result = films.values();
        log.info("Получен список всех фильмов в количестве - {}!", result.size());
        return result;
    }

    @PostMapping
    public Film add(@NotNull @Valid @RequestBody Film film) {
        log.debug("Отправлен запрос на добавление фильма!");
        if (films.values().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(film.getName()))) {
            log.warn("Ошибка добавления фильма {}! Причина - фильм с идентичным названием уже есть в БД!",
                    film.getName());
            throw new DuplicatedDataException("Фильм с указанным названием уже добавлен");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен, присвоен id {}!", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film update(@NotNull @Valid @RequestBody Film film) {
        log.debug("Отправлен запрос на обновление фильма!");
        if (film.getId() == null) {
            log.warn("Ошибка при обновлении фильма! Причина - не указан id!");
            throw new ValidationException("id должен быть указан");
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Ошибка при обновлении фильма! Причина - фильм с id {} не найден!", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        films.replace(film.getId(), film);
        log.info("Информация фильма с id {} обновлена!", film.getId());
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Максимальный id = {}", currentMaxId);
        return ++currentMaxId;
    }
}

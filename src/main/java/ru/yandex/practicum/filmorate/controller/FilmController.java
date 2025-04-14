package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.util.*;

@Getter
@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    // Геттеры добавлены с целью очистки наполнения для выполнения тестов
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> addFilms = new HashSet<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Отправлен запрос на получение списка фильмов");
        Collection<Film> result = films.values();
        log.info("Получен список всех фильмов в количестве - {}!", result.size());
        return result;
    }

    @PostMapping
    public Film add(@NotNull @Validated(ValidationGroups.PostValidationGroup.class) @RequestBody Film film) {
        log.debug("Отправлен запрос на добавление фильма!");
        String checkName = film.getName().toLowerCase();
        if (addFilms.contains(checkName)) {
            log.warn("Ошибка добавления фильма {}! Причина - фильм уже есть в БД!",
                    film.getName());
            throw new DuplicatedDataException("Указанный фильм уже добавлен");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен, присвоен id {}!", film.getName(), film.getId());
        addFilms.add(checkName);
        log.debug("Фильм {} добавлен в хранилище добавленных", checkName);
        return film;
    }

    @PutMapping
    public Film update(@NotNull @Validated(ValidationGroups.PutValidationGroup.class) @RequestBody Film film) {
        log.debug("Отправлен запрос на обновление фильма!");
        Long idCheck = film.getId();
        if (idCheck == null) {
            log.warn("Ошибка при обновлении фильма! Причина - не указан id!");
            throw new ValidationException("id должен быть указан");
        }
        if (!films.containsKey(idCheck)) {
            log.warn("Ошибка при обновлении фильма! Причина - фильм с id {} не найден!", film.getId());
            throw new NotFoundException("Фильм с id " + idCheck + " не найден");
        }
        String nameOld = films.get(idCheck).getName().toLowerCase().trim();
        log.debug("Название фильма при добавлении - {}", nameOld);
        String nameNew = film.getName().toLowerCase().trim();
        log.debug("Название фильма при обновлении - {}", nameNew);
        if (!nameNew.equals(nameOld)) {
            log.info("Название фильма при обновлении отличается от названия при добавлении");
            if (addFilms.contains(nameNew)) {
                log.warn("Ошибка при обновлении фильма! Причина - фильм с названием {} уже есть в БД!",
                        nameNew);
                throw new DuplicatedDataException("Указанный фильм уже добавлен");
            }
            addFilms.remove(nameOld);
            log.debug("Фильм {} удален из хранилища добавленных", nameOld);
            addFilms.add(nameNew);
            log.debug("Фильм {} добавлен в хранилище добавленных", nameNew);
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

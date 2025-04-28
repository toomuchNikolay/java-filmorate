package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService service;

    @PostMapping
    public ResponseEntity<Film> addFilm(
            @Validated(ValidationGroups.PostValidationGroup.class) @RequestBody Film film
    ) {
        log.debug("Получен запрос на добавление фильма {}", film);
        Film added = service.addFilm(film);
        log.debug("Запрос на добавление фильма успешно обработан");
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(
            @Validated(ValidationGroups.PutValidationGroup.class) @RequestBody Film film
    ) {
        log.debug("Получен запрос на обновление фильма с id {}", film.getId());
        Film updated = service.updateFilm(film);
        log.debug("Запрос на обновление фильма успешно обработан");
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id,
            @PathVariable @Positive(message = "id должен быть больше нуля") Long userId
    ) {
        log.debug("Получен запрос на добавление лайка фильму {} от пользователя {}", id, userId);
        service.addLike(id, userId);
        log.debug("Запрос на добавление лайка успешно обработан");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id,
            @PathVariable @Positive(message = "id должен быть больше нуля") Long userId
    ) {
        log.debug("Получен запрос на удаление лайка у фильма {} от пользователя {}", id, userId);
        service.removeLike(id, userId);
        log.debug("Запрос на удаление лайка успешно обработан");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.debug("Получен запрос на формирование списка всех фильмов");
        Collection<Film> allFilms = service.getAllFilms();
        log.debug("Запрос на формирование списка всех фильмов успешно обработан");
        return ResponseEntity.ok(allFilms);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getPopularFilms(
            @RequestParam(defaultValue = "10")
            @Positive(message = "Размер списка должен быть больше нуля")
            Integer count
    ) {
        log.debug("Получен запрос на формирование списка популярных фильмов");
        Collection<Film> topFilms = service.getPopularFilms(count);
        log.debug("Запрос на формирование списка популярных фильмов успешно обработан");
        return ResponseEntity.ok(topFilms);
    }
}

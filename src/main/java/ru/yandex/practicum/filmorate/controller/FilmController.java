package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto addFilm(@RequestBody @Valid NewFilmRequest film) {
        log.info("Запрос на добавление фильма: {}", film);
        FilmDto added = filmService.addFilm(film);
        log.info("Запрос успешно обработан, добавлен фильм: {}", added);
        return added;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto updateFilm(@RequestBody @Valid UpdateFilmRequest film) {
        log.info("Запрос на обновление фильма: {}", film);
        FilmDto updated = filmService.updateFilm(film);
        log.info("Запрос успешно обработан, обновлен фильм: {}", updated);
        return updated;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getAllFilms() {
        log.info("Запрос на получение списка всех фильмов");
        Collection<FilmDto> collection = filmService.getAllFilms();
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilmById(@PathVariable Long filmId) {
        log.info("Запрос на получение фильма id = {}", filmId);
        FilmDto findFilm = filmService.getFilmById(filmId);
        log.info("Запрос успешно обработан, получен фильм: {}", findFilm);
        return findFilm;
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Запрос на добавление лайка фильму id = {} от пользователя id = {}", filmId, userId);
        likeService.addLike(filmId, userId);
        log.info("Запрос успешно обработан, лайк добавлен");
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Запрос на удаление лайка фильму id = {} от пользователя id = {}", filmId, userId);
        likeService.removeLike(filmId, userId);
        log.info("Запрос успешно обработан, лайк добавлен");
    }

    @GetMapping("/{filmId}/like")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getFilmLikes(@PathVariable Long filmId) {
        log.info("Запрос на получение списка пользователей, лайкнувших фильм id = {}", filmId);
        Collection<UserDto> collection = likeService.getFilmLikes(filmId);
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getPopularFilms(
            @RequestParam(defaultValue = "10")
            @Positive(message = "Размер списка должен быть больше нуля")
            Integer count
    ) {
        log.info("Запрос на получение списка популярных фильмов в количестве {}", count);
        Collection<FilmDto> collection = likeService.getPopularFilms(count);
        log.info("Запрос успешно обработан");
        return collection;
    }
}

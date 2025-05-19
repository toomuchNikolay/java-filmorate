package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeStorage storage;
    private final FilmService filmService;
    private final UserService userService;

    public void addLike(Long filmId, Long userId) {
        filmService.getFilmById(filmId);
        userService.getUserById(userId);
        Optional<Like> alreadyExistLike = storage.findLike(filmId, userId);
        if (alreadyExistLike.isPresent()) {
            log.error("Попытка повторно поставить лайка фильму id = {} пользователем id = {}", filmId, userId);
            throw new DuplicatedDataException("Лайк фильму от пользователя уже проставлен");
        }
        Like like = Like.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
        storage.addLike(like);
        log.debug("Добавлена запись лайка: {}", like);
    }

    public void removeLike(Long filmId, Long userId) {
        Like like = getLike(filmId, userId);
        storage.removeLike(like);
        log.debug("Удалена запись лайка: {}", like);
    }

    public Collection<FilmDto> getUserLikes(Long userId) {
        userService.getUserById(userId);
        return storage.findUserLikes(userId).stream()
                .map(filmService::getFilmById)
                .toList();
    }

    public Collection<UserDto> getFilmLikes(Long filmId) {
        filmService.getFilmById(filmId);
        return storage.findFilmLikes(filmId).stream()
                .map(userService::getUserById)
                .toList();
    }

    public Collection<FilmDto> getPopularFilms(Integer count) {
        return storage.findPopularFilms(count).stream()
                .map(filmService::getFilmById)
                .toList();
    }

    private Like getLike(Long filmId, Long userId) {
        return storage.findLike(filmId, userId)
                .orElseThrow(() -> new NotFoundException("Лайк не найден"));
    }
}

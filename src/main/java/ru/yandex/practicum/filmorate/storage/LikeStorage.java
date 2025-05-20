package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;
import java.util.Optional;

public interface LikeStorage {
    void addLike(Like like);

    void removeLike(Like like);

    Collection<Long> findUserLikes(Long userId);

    Collection<Long> findFilmLikes(Long filmId);

    Collection<Long> findPopularFilms(Integer count);

    Optional<Like> findLike(Long filmId, Long userId);
}

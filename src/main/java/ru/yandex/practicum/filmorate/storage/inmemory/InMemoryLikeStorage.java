package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryLikeStorage implements LikeStorage {
    private final Set<Like> likes = new HashSet<>();

    @Override
    public void addLike(Like like) {
        likes.add(like);
    }

    @Override
    public void removeLike(Like like) {
        likes.remove(like);
    }

    @Override
    public Collection<Long> findUserLikes(Long userId) {
        return likes.stream()
                .filter(l -> l.getUserId().equals(userId))
                .map(Like::getFilmId)
                .toList();
    }

    @Override
    public Collection<Long> findFilmLikes(Long filmId) {
        return likes.stream()
                .filter(l -> l.getFilmId().equals(filmId))
                .map(Like::getUserId)
                .toList();
    }

    @Override
    public Collection<Long> findPopularFilms(Integer count) {
        return likes.stream()
                .collect(Collectors.groupingBy(Like::getFilmId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .toList();
    }

    @Override
    public Optional<Like> findLike(Long filmId, Long userId) {
        return likes.stream()
                .filter(l -> l.getFilmId().equals(filmId) && l.getUserId().equals(userId))
                .findFirst();
    }
}

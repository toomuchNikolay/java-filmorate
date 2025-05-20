package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DbLikeStorage extends BaseDbStorage<Like> implements LikeStorage {
    private static final String INSERT = "INSERT INTO film_likes(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String FIND_USER_LIKES = "SELECT film_id FROM film_likes WHERE user_id = ?";
    private static final String FIND_FILM_LIKES = "SELECT user_id FROM film_likes WHERE film_id = ?";
    private static final String FIND_POPULAR_FILMS = "SELECT film_id FROM film_likes GROUP BY film_id " +
            "ORDER BY COUNT(user_id) DESC LIMIT ?";
    private static final String FIND_LIKE = "SELECT * FROM film_likes WHERE film_id = ? AND user_id = ?";

    public DbLikeStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(Like like) {
        insertWithoutKey(INSERT,
                like.getFilmId(),
                like.getUserId()
                );
    }

    @Override
    public void removeLike(Like like) {
        delete(DELETE,
                like.getFilmId(),
                like.getUserId()
        );
    }

    @Override
    public Collection<Long> findUserLikes(Long userId) {
        return jdbc.queryForList(FIND_USER_LIKES, Long.class, userId);
    }

    @Override
    public Collection<Long> findFilmLikes(Long filmId) {
        return jdbc.queryForList(FIND_FILM_LIKES, Long.class, filmId);
    }


    @Override
    public Collection<Long> findPopularFilms(Integer count) {
        return jdbc.queryForList(FIND_POPULAR_FILMS, Long.class, count);
    }

    @Override
    public Optional<Like> findLike(Long filmId, Long userId) {
        return getOne(FIND_LIKE, filmId, userId);
    }
}

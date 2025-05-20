package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mappers.LikeRowMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbLikeStorage.class, LikeRowMapper.class})
class DbLikeStorageTest {
    private final DbLikeStorage storage;

    private Like createLike() {
        return Like.builder()
                .filmId(1L)
                .userId(1L)
                .build();
    }

    @Test
    void addLike() {
        storage.addLike(createLike());
        Optional<Like> findLike = storage.findLike(1L, 1L);

        assertThat(findLike).isPresent();
        assertThat(findLike.get().getFilmId()).isEqualTo(1L);
        assertThat(findLike.get().getUserId()).isEqualTo(1L);
    }

    @Test
    void removeLike() {
        storage.addLike(createLike());
        storage.addLike(Like.builder()
                .filmId(2L)
                .userId(1L)
                .build()
        );

        assertThat(storage.findPopularFilms(5))
                .hasSize(2);

        storage.removeLike(createLike());
        Optional<Like> findLike = storage.findLike(1L, 1L);

        assertThat(storage.findPopularFilms(5))
                .hasSize(1);
        assertThat(findLike).isEmpty();
    }

    @Test
    void findUserLikes() {
        storage.addLike(createLike());
        storage.addLike(Like.builder()
                .filmId(2L)
                .userId(1L)
                .build()
        );
        Collection<Long> collection = storage.findUserLikes(1L);

        assertThat(collection).contains(1L, 2L);
    }

    @Test
    void findFilmLikes() {
        storage.addLike(createLike());
        storage.addLike(Like.builder()
                .filmId(1L)
                .userId(3L)
                .build()
        );
        Collection<Long> collection = storage.findFilmLikes(1L);

        assertThat(collection).contains(1L, 3L);
    }

    @Test
    void findPopularFilms() {
        storage.addLike(createLike());
        storage.addLike(Like.builder()
                .filmId(2L)
                .userId(1L)
                .build()
        );
        storage.addLike(Like.builder()
                .filmId(3L)
                .userId(2L)
                .build()
        );
        storage.addLike(Like.builder()
                .filmId(3L)
                .userId(3L)
                .build()
        );
        storage.addLike(Like.builder()
                .filmId(1L)
                .userId(4L)
                .build()
        );
        storage.addLike(Like.builder()
                .filmId(3L)
                .userId(4L)
                .build()
        );
        Collection<Long> collection = new ArrayList<>(storage.findPopularFilms(5));

        assertThat(collection)
                .element(0).isEqualTo(3L);
        assertThat(collection)
                .element(1).isEqualTo(1L);
        assertThat(collection)
                .element(2).isEqualTo(2L);
    }
}
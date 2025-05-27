package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.mappers.FriendshipRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbFriendshipStorage.class, FriendshipRowMapper.class})
class DbFriendshipStorageTest {
    private final DbFriendshipStorage storage;

    private Friendship createFriendship() {
        return Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .build();
    }

    @Test
    void addFriendship() {
        storage.addFriendship(createFriendship());
        Optional<Friendship> findFriendship = storage.findFriendship(1L, 2L);

        assertThat(findFriendship).isPresent();
        assertThat(findFriendship.get().getUserId()).isEqualTo(1L);
        assertThat(findFriendship.get().getFriendId()).isEqualTo(2L);
        assertThat(findFriendship.get().getStatus()).isEqualTo(FriendshipStatus.PENDING);
    }

    @Test
    void removeFriendship() {
        storage.addFriendship(createFriendship());
        storage.addFriendship(Friendship.builder()
                .userId(1L)
                .friendId(3L)
                .build());

        assertThat(storage.findFriends(1L))
                .hasSize(2);

        storage.removeFriendship(createFriendship());
        Optional<Friendship> findFriendship = storage.findFriendship(1L, 2L);

        assertThat(storage.findFriends(1L))
                .hasSize(1);
        assertThat(findFriendship).isEmpty();
    }

    @Test
    void findCommonFriends() {
        storage.addFriendship(createFriendship());
        storage.addFriendship(Friendship.builder()
                .userId(3L)
                .friendId(2L)
                .build());
        Collection<Long> collection = storage.findCommonFriends(1L, 3L);

        assertThat(collection)
                .contains(2L);
    }
}
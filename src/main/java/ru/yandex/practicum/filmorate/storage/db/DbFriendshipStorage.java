package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DbFriendshipStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private static final String INSERT = "INSERT INTO friendships(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS = "SELECT friend_id FROM friendships WHERE user_id = ?";
    private static final String FIND_COMMON_FRIENDS = "SELECT f1.friend_id FROM friendships f1 " +
            "JOIN friendships f2 ON f1.friend_id = f2.friend_id WHERE f1.user_id = ? AND f2.user_id = ?";
    private static final String FIND_FRIENDSHIP = "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?";

    public DbFriendshipStorage(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriendship(Friendship friendship) {
        insertWithoutKey(INSERT,
                friendship.getUserId(),
                friendship.getFriendId()
                );
    }

    @Override
    public void removeFriendship(Friendship friendship) {
        delete(DELETE,
                friendship.getUserId(),
                friendship.getFriendId()
                );
    }

    @Override
    public Collection<Long> findFriends(Long userId) {
        return jdbc.queryForList(FIND_FRIENDS, Long.class, userId);
    }

    @Override
    public Collection<Long> findCommonFriends(Long userId, Long otherId) {
        return jdbc.queryForList(FIND_COMMON_FRIENDS, Long.class, userId, otherId);
    }

    @Override
    public Optional<Friendship> findFriendship(Long userId, Long friendId) {
        return getOne(FIND_FRIENDSHIP, userId, friendId);
    }
}

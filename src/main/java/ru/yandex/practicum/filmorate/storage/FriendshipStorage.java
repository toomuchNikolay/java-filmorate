package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipStorage {
    void addFriendship(Friendship friendship);

    void removeFriendship(Friendship friendship);

    Collection<Long> findFriends(Long userId);

    Collection<Long> findCommonFriends(Long userId, Long otherId);

    Optional<Friendship> findFriendship(Long userId, Long friendId);
}

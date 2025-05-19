package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final Set<Friendship> friends = new HashSet<>();

    @Override
    public void addFriendship(Friendship friendship) {
        friends.add(friendship);
    }

    @Override
    public void removeFriendship(Friendship friendship) {
        friends.remove(friendship);
    }

    @Override
    public Collection<Long> findFriends(Long userId) {
        return friends.stream()
                .filter(f -> f.getUserId().equals(userId))
                .map(Friendship::getFriendId)
                .toList();
    }

    @Override
    public Collection<Long> findCommonFriends(Long userId, Long otherId) {
        Collection<Long> userFriends = friends.stream()
                .filter(f -> f.getUserId().equals(userId))
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
        Collection<Long> otherUserFriends = friends.stream()
                .filter(f -> f.getUserId().equals(otherId))
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }


    @Override
    public Optional<Friendship> findFriendship(Long userId, Long friendId) {
        return friends.stream()
                .filter(f -> f.getUserId().equals(userId) && f.getFriendId().equals(friendId))
                .findFirst();
    }
}

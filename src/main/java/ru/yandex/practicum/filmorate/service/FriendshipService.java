package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {
    private final FriendshipStorage storage;
    private final UserService userService;

    public void addFriend(Long userId, Long friendId) {
        validate(userId, friendId);
        Optional<Friendship> alreadyExistFriendship = storage.findFriendship(userId, friendId);
        alreadyExistFriendship.ifPresent(f -> {
            if (f.getStatus() == FriendshipStatus.PENDING) {
                log.warn("Отправлен повторный запрос на добавление в друзья");
                throw new DuplicatedDataException("Запрос на добавление пользователя в друзья уже отправлен");
            }
            if (f.getStatus() == FriendshipStatus.CONFIRMED) {
                log.warn("Отправлен запрос другу на добавление в друзья");
                throw new DuplicatedDataException("Пользователь уже добавлен в друзья");
            }
        });
        Friendship friendship = Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .status(FriendshipStatus.PENDING)
                .build();
        Optional<Friendship> checkRequestFriendship = storage.findFriendship(friendId, userId);
        if (checkRequestFriendship.isPresent()) {
            log.info("Найден встречный запрос от пользователя, которого добавляют в друзья");
            updateFriendship(checkRequestFriendship.get(), FriendshipStatus.CONFIRMED);
            friendship.setStatus(FriendshipStatus.CONFIRMED);
        }
        storage.addFriendship(friendship);
        log.debug("Добавлена запись дружбы: {}", friendship);
    }

    public void removeFriend(Long userId, Long friendId) {
        validate(userId, friendId);
        Optional<Friendship> findFriendship = storage.findFriendship(userId, friendId);
        findFriendship.ifPresent(f -> {
            if (f.getStatus() == FriendshipStatus.CONFIRMED) {
                Optional<Friendship> reverseFriendship = storage.findFriendship(friendId, userId);
                updateFriendship(reverseFriendship.get(), FriendshipStatus.PENDING);
            }
            storage.removeFriendship(f);
            log.debug("Удалена запись дружбы: {}", f);
        });
    }

    public Collection<UserDto> getFriends(Long userId) {
        userService.getUserById(userId);
        return storage.findFriends(userId).stream()
                .map(userService::getUserById)
                .toList();
    }

    public Collection<UserDto> getCommonFriends(Long userId, Long otherId) {
        return storage.findCommonFriends(userId, otherId).stream()
                .map(userService::getUserById)
                .toList();
    }

    private void validate(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            log.warn("Не указан id при добавлении в друзья");
            throw new ValidationException("id пользователей должны быть указаны");
        }
        if (userId.equals(friendId)) {
            log.warn("Указан одинаковый пользователь при добавлении в друзья");
            throw new ValidationException("У пользователя и друга не может быть одинаковый id");
        }
        userService.getUserById(userId);
        userService.getUserById(friendId);
    }

    private void updateFriendship(Friendship friendship, FriendshipStatus status) {
        storage.removeFriendship(friendship);
        log.debug("Удалена запись дружбы: {}", friendship);
        Friendship updated = Friendship.builder()
                .userId(friendship.getUserId())
                .friendId(friendship.getFriendId())
                .status(status)
                .build();
        storage.addFriendship(updated);
        log.debug("Добавлена запись дружбы: {}", updated);
    }
}

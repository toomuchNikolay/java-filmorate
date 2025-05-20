package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequest user) {
        log.info("Запрос на добавление пользователя: {}", user);
        UserDto added = userService.addUser(user);
        log.info("Запрос успешно обработан, добавлен пользователь: {}", added);
        return added;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@RequestBody @Valid UpdateUserRequest user) {
        log.info("Запрос на обновление пользователя: {}", user);
        UserDto updated = userService.updateUser(user);
        log.info("Запрос успешно обработан, обновлен пользователь: {}", updated);
        return updated;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        Collection<UserDto> collection = userService.getAllUsers();
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUsersById(@PathVariable Long userId) {
        log.info("Запрос на получение пользователя id = {}", userId);
        UserDto findUser = userService.getUserById(userId);
        log.info("Запрос успешно обработан, получен пользователь: {}", findUser);
        return findUser;
    }

    @GetMapping("/{userId}/like")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getUserLikes(@PathVariable Long userId) {
        log.info("Запрос на получение списка всех лайков пользователя id = {}", userId);
        Collection<FilmDto> collection = likeService.getUserLikes(userId);
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Запрос пользователя id = {} на добавление в друзья пользователя id {}", userId, friendId);
        friendshipService.addFriend(userId, friendId);
        log.info("Запрос успешно обработан, дружба добавлена");
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Запрос пользователя id = {} на удаление из друзей пользователя id {}", userId, friendId);
        friendshipService.removeFriend(userId, friendId);
        log.info("Запрос успешно обработан, дружба удалена");
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getFriends(@PathVariable Long userId) {
        log.info("Запрос на получение списка всех друзей пользователя id = {}", userId);
        Collection<UserDto> collection = friendshipService.getFriends(userId);
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Запрос на получение списка общих друзей пользователей id = {} и id = {}", userId, otherId);
        Collection<UserDto> collection = friendshipService.getCommonFriends(userId, otherId);
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }
}

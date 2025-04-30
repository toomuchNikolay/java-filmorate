package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<User> addUser(
            @Validated(ValidationGroups.PostValidationGroup.class) @RequestBody User user
    ) {
        log.debug("Получен запрос на добавление пользователя {}", user);
        User added = service.addUser(user);
        log.debug("Запрос на добавление пользователя успешно обработан");
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(
            @Validated(ValidationGroups.PutValidationGroup.class) @RequestBody User user
    ) {
        log.debug("Получен запрос на обновление пользователя с id {}", user.getId());
        User updated = service.updateUser(user);
        log.debug("Запрос на обновление пользователя успешно обработан");
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id,
            @PathVariable @Positive(message = "id должен быть больше нуля") Long friendId
    ) {
        log.debug("Получен запрос от пользователя {} на добавление в друзья пользователя {}", id, friendId);
        service.addFriend(id, friendId);
        log.debug("Запрос на добавление в друзья успешно обработан");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id,
            @PathVariable @Positive(message = "id должен быть больше нуля") Long friendId
    ) {
        log.debug("Получен запрос от пользователя {} на удаление из друзей пользователя {}", id, friendId);
        service.removeFriend(id, friendId);
        log.debug("Запрос на удаление из друзей успешно обработан");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.debug("Получен запрос на формирование списка всех пользователей");
        Collection<User> allUsers = service.getAllUsers();
        log.debug("Запрос на формирование списка всех пользователей успешно обработан");
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id
    ) {
        log.debug("Получен запрос на формирование списка друзей пользователя {}", id);
        Collection<User> friends = service.getFriends(id);
        log.debug("Запрос на формирование списка друзей пользователя успешно обработан");
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(
            @PathVariable @Positive(message = "id должен быть больше нуля") Long id,
            @PathVariable @Positive(message = "id должен быть больше нуля") Long otherId
    ) {
        log.debug("Получен запрос от пользователя {} на формирование списка общих друзей с пользователем {}", id, otherId);
        Collection<User> commonFriends = service.getCommonFriends(id, otherId);
        log.debug("Запрос на формирование списка общих друзей успешно обработан");
        return ResponseEntity.ok(commonFriends);
    }
}

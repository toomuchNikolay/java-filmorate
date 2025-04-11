package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Отправлен запрос на получение списка пользователей");
        Collection<User> result = users.values();
        log.info("Получен список всех пользователей в количестве - {}!", result.size());
        return result;
    }

    @PostMapping
    public User add(@NotNull @Valid @RequestBody User user) {
        log.debug("Отправлен запрос на добавление пользователя!");
        if (users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))) {
            log.warn("Ошибка при добавлении пользователя с электронной почтой {}! " +
                            "Причина - пользователь с идентичной электронной почтой уже есть в БД!", user.getEmail());
            throw new DuplicatedDataException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с электронной почтой {} добавлен, присвоен id {}!", user.getEmail(), user.getId());
        return user;
    }

    @PutMapping
    public User update(@NotNull @Valid @RequestBody User user) {
        log.debug("Отправлен запрос на обновление пользователя!");
        if (user.getId() == null) {
            log.warn("Ошибка при обновлении пользователя! Причина - не указан id!");
            throw new ValidationException("id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Ошибка при обновлении пользователя! Причина - пользователь с id {} не найден!", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        users.replace(user.getId(), user);
        log.info("Информация пользователя с id {} обновлена!", user.getId());
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Максимальный id = {}", currentMaxId);
        return ++currentMaxId;
    }
}

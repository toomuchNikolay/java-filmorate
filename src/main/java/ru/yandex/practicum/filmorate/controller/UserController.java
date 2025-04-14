package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.util.*;

@Getter
@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    // Геттеры добавлены с целью очистки наполнения для выполнения тестов
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> registeredEmail = new HashSet<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Отправлен запрос на получение списка пользователей");
        Collection<User> result = users.values();
        log.info("Получен список всех пользователей в количестве - {}!", result.size());
        return result;
    }

    @PostMapping
    public User add(@NotNull @Validated(ValidationGroups.PostValidationGroup.class) @RequestBody User user) {
        log.debug("Отправлен запрос на добавление пользователя!");
        String checkMail = user.getEmail().toLowerCase().trim();
        if (registeredEmail.contains(checkMail)) {
            log.warn("Ошибка при добавлении пользователя с электронной почтой {}! " +
                    "Причина - пользователь с идентичной электронной почтой уже есть в БД!", user.getEmail());
            throw new DuplicatedDataException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использоваться логин");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с электронной почтой {} добавлен, присвоен id {}!", user.getEmail(), user.getId());
        registeredEmail.add(checkMail);
        log.debug("Электронная почта {} добавлена в хранилище используемых", checkMail);
        return user;
    }

    @PutMapping
    public User update(@NotNull @Validated(ValidationGroups.PutValidationGroup.class) @RequestBody User user) {
        log.debug("Отправлен запрос на обновление пользователя!");
        Long idCheck = user.getId();
        if (idCheck == null) {
            log.warn("Ошибка при обновлении пользователя! Причина - не указан id!");
            throw new ValidationException("id должен быть указан");
        }
        if (!users.containsKey(idCheck)) {
            log.warn("Ошибка при обновлении пользователя! Причина - пользователь с id {} не найден!", user.getId());
            throw new NotFoundException("Пользователь с id " + idCheck + " не найден");
        }
        String mailOld = users.get(idCheck).getEmail().toLowerCase().trim();
        log.debug("Электронная почта пользователя при регистрации - {}", mailOld);
        String mailNew = user.getEmail().toLowerCase().trim();
        log.debug("Электронная почта пользователя при обновлении - {}", mailNew);
        if (!mailNew.equals(mailOld)) {
            log.info("Указанная электронная почта отличается от указанной при регистрации");
            if (registeredEmail.contains(mailNew)) {
                log.warn("Ошибка при обновлении пользователя! " +
                        "Причина - пользователь с электронной почтой {} уже есть в БД!", mailNew);
                throw new DuplicatedDataException("Пользователь с указанной электронной почтой уже зарегистрирован");
            }
            registeredEmail.remove(mailOld);
            log.debug("Электронная почта {} удалена из хранилища используемых", mailOld);
            registeredEmail.add(mailNew);
            log.debug("Электронная почта {} добавлена в хранилище используемых", mailNew);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использоваться логин");
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

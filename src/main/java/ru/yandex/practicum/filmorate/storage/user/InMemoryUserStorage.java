package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Getter
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usedEmails = new HashSet<>();

    @Override
    public User addUser(User user) {
        log.debug("Добавление в хранилище пользователя {}", user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь {} успешно добавлен под id {}", user, user.getId());
        usedEmails.add(user.getEmail().toLowerCase().trim());
        log.debug("В список зарегистрированных электронных почт добавлено {}", user.getEmail().toLowerCase().trim());
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Обновление в хранилище пользователя {} с id {}", user, user.getId());
        users.replace(user.getId(), user);
        log.info("Пользователь {} в хранилище успешно обновлен", user);
        String oldEmail = users.get(user.getId()).getEmail().toLowerCase().trim();
        String newEmail = user.getEmail().toLowerCase().trim();
        if (!oldEmail.equals(newEmail)) {
            usedEmails.remove(oldEmail);
            log.debug("Из списка зарегистрированных электронных почт удалена {}", oldEmail);
            usedEmails.add(newEmail);
            log.debug("В список зарегистрированных электронных почт добавлено {}", newEmail);
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> result = users.values();
        log.info("Список всех пользователей успешно получен");
        return result;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean isUsedMail(String email) {
        return usedEmails.contains(email.toLowerCase().trim());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

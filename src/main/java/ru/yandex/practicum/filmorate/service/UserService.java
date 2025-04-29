package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage storage;

    public User addUser(User user) {
        log.debug("Попытка добавить пользователя {}", user);
        if (storage.isUsedMail(user.getEmail().toLowerCase().trim())) {
            log.warn("Попытка добавить пользователя на используемую электронную почту {}", user.getEmail());
            throw new DuplicatedDataException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }
        if (!user.getFriends().isEmpty()) {
            log.warn("Попытка добавить пользователя {} c заполненным списком друзей", user);
            throw new ValidationException("У нового пользователя список друзей должен быть пустой");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использоваться логин {}", user.getLogin());
        }
        User added = storage.addUser(user);
        log.info("Пользователь {} добавлен под id {}", added, added.getId());
        return added;
    }

    public User updateUser(User user) {
        log.debug("Попытка обновить пользователя {}", user);
        User oldUser = getUserById(user.getId());
        String mailOld = oldUser.getEmail().toLowerCase().trim();
        String mailNew = user.getEmail().toLowerCase().trim();
        if (!mailNew.equals(mailOld)) {
            if (storage.isUsedMail(mailNew)) {
                log.warn("Попытка добавить используемую электронную почту {}", mailNew);
                throw new DuplicatedDataException("Пользователь с указанной электронной почтой уже зарегистрирован");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использоваться логин {}", user.getLogin());
        }
        User updated = storage.updateUser(user);
        log.info("Информация о пользователе {} обновлена", updated);
        return updated;
    }

    public Collection<User> getAllUsers() {
        log.debug("Попытка получить список всех пользователей");
        Collection<User> result = storage.getAllUsers();
        log.info("Список всех пользователей в размере {} получен", result.size());
        return result;
    }

    public User getUserById(Long id) {
        return storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
    }

    public void addFriend(Long id, Long friendId) {
        log.debug("Попытка пользователя {} добавить в список друзей пользователя {}", id, friendId);
        validate(id, friendId);
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (user.getFriends().contains(friendId)) {
            log.warn("Попытка добавить уже присутствующего в друзьях пользователя {}", friendId);
            throw new DuplicatedDataException("Пользователь " + friendId + " уже добавлен в друзья");
        }
        user.getFriends().add(friendId);
        storage.updateUser(user);
        log.info("Пользователь {} добавлен в список друзей пользователя {}", friendId, id);
        friend.getFriends().add(id);
        storage.updateUser(friend);
        log.info("Пользователь {} добавлен в список друзей пользователя {}", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        log.debug("Попытка пользователя {} удалить из списка друзей пользователя {}", id, friendId);
        validate(id, friendId);
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        storage.updateUser(user);
        log.info("Пользователь {} удален из списка друзей пользователя {}", friendId, id);
        friend.getFriends().remove(id);
        storage.updateUser(friend);
        log.info("Пользователь {} удален из списка друзей пользователя {}", id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.debug("Попытка получить список друзей пользователя {}", id);
        User user = getUserById(id);
        Collection<User> result = user.getFriends().stream()
                .map(storage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.info("Список друзей пользователя {} в размере {} получен", id, result.size());
        return result;
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.debug("Попытка получить список общих друзей пользователя {} с пользователем {}", id, otherId);
        validate(id, otherId);
        User user = getUserById(id);
        User friend = getUserById(otherId);
        Collection<User> result = user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.info("Список общих друзей пользователей {} и {} в размере {} получен", id, otherId, result.size());
        return result;
    }

    private void validate(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            log.warn("Попытка указать одного и того же пользователя");
            throw new ValidationException("У пользователя и друга не может быть одинаковый id");
        }
    }
}

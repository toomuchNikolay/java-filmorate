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
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        log.debug("Попытка обновить пользователя {}", user);
        User oldUser = storage.findUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + user.getId() + " не найден"));
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
        return storage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        log.debug("Попытка получить список всех пользователей");
        return storage.getAllUsers();
    }

    public User getUserById(Long id) {
        return storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
    }

    public void addFriend(Long id, Long friendId) {
        log.debug("Попытка добавить пользователем {} в друзья {}", id, friendId);
        validate(id, friendId);
        User user = storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
        User friend = storage.findUserById(friendId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", friendId);
                    return new NotFoundException("Пользователь с id " + friendId + " не найден");
                });
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
        validate(id, friendId);
        User user = storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
        User friend = storage.findUserById(friendId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", friendId);
                    return new NotFoundException("Пользователь с id " + friendId + " не найден");
                });
//        С данной проверкой падает тест DELETE Not friend remove
//        Не ясна логика - если пользователь в друзьях не найден, то надо вернуть 200 или 204
//        if (!user.getFriends().contains(friendId)) {
//            throw new NotFoundException("Пользователь " + friendId + " не найден в списке друзей");
//        }
        user.getFriends().remove(friendId);
        storage.updateUser(user);
        log.info("Пользователь {} удален из списка друзей пользователя {}", friendId, id);
        friend.getFriends().remove(id);
        storage.updateUser(friend);
        log.info("Пользователь {} удален из списка друзей пользователя {}", id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        User user = storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
        log.debug("Попытка получить список друзей пользователя {}", id);
        return user.getFriends().stream()
                .map(storage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        validate(id, otherId);
        User user = storage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
        User friend = storage.findUserById(otherId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", otherId);
                    return new NotFoundException("Пользователь с id " + otherId + " не найден");
                });
        log.debug("Попытка получить список общих друзей пользователя {} с пользователем {}", id, otherId);
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private void validate(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            log.warn("Попытка указать одного и того же пользователя");
            throw new ValidationException("У пользователя и друга не может быть одинаковый id");
        }
    }
}

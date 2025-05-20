package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserDto addUser(NewUserRequest user) {
        User added = UserMapper.mapToUser(user);
        isNameEmpty(added);
        added = userStorage.addUser(added);
        log.debug("Добавлена запись пользовтаеля: {}", added);
        return UserMapper.mapToUserDto(added);
    }

    public UserDto updateUser(UpdateUserRequest user) {
        User updated = userStorage.findUserById(user.getId())
                .map(u -> UserMapper.updateUserFields(u, user))
                .orElseThrow(() -> {
                    log.error("Попытка обновить несуществующего пользователя");
                    return new NotFoundException("Пользователь не найден");
                });
        isNameEmpty(updated);
        updated = userStorage.updateUser(updated);
        log.debug("Обновлена запись пользователя: {}", updated);
        return UserMapper.mapToUserDto(updated);
    }

    public Collection<UserDto> getAllUsers() {
        return userStorage.findAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto getUserById(Long userId) {
        return userStorage.findUserById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

    }

    private void isNameEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, использован логин");
        }
    }
}

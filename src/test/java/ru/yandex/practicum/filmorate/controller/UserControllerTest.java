package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController controller;

    private User user;

    @BeforeEach
    void setUp() {
        controller.getAllUsers().clear();
        user = new User();
        user.setEmail("TesT@mail.com");
        user.setLogin("login");
        user.setName("test name");
        user.setBirthday(LocalDate.of(2000, 1, 10));
    }

    @Test
    void checkMethodAdd() {
        user.setName(" ");
        controller.add(user);

        assertNotNull(user.getId(), "При добавлении пользователя не присвоился id");
        assertTrue(controller.getAllUsers().contains(user), "Пользователь не добавился в БД");
        assertEquals(user.getLogin(), user.getName(), "При пустом имени пользователя не добавился логин");

        User duplicate = new User();
        duplicate.setEmail(user.getEmail().toLowerCase());
        duplicate.setLogin("duplicate");
        duplicate.setName("name");
        duplicate.setBirthday(LocalDate.of(2010, 10, 10));

        DuplicatedDataException e = assertThrows(DuplicatedDataException.class, () -> controller.add(duplicate));
        assertEquals("Пользователь с указанной электронной почтой уже зарегистрирован", e.getMessage(),
                "Не сработало исключение при добавлении пользователя с аналогичной электронной почтой");
    }

    @Test
    void checkMethodUpdate() {
        controller.add(user);

        assertEquals(1, controller.getAllUsers().size(), "Не добавился пользователь в БД");

        User updateUser = new User();
        updateUser.setEmail(user.getEmail());
        updateUser.setLogin("newUserLogin");
        updateUser.setName(user.getName());
        updateUser.setBirthday(user.getBirthday());

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.update(updateUser));
        assertEquals("id должен быть указан", exception.getMessage(),
                "Не сработало исключение при обновлении пользователя без указания id");

        updateUser.setId(2L);

        NotFoundException e = assertThrows(NotFoundException.class, () -> controller.update(updateUser));
        assertEquals("Пользователь с id 2 не найден", e.getMessage(),
                "Не сработало исключение при обновлении пользователя с указанием несуществующего id");

        updateUser.setId(user.getId());
        controller.update(updateUser);

        assertEquals(1, controller.getAllUsers().size(), "Изменилось количество в БД");

        assertFalse(controller.getAllUsers().stream()
                        .anyMatch(u -> u.getLogin().equalsIgnoreCase("login")),
                "В БД есть пользователь со старым логином");
        assertTrue(controller.getAllUsers().stream()
                        .anyMatch(u -> u.getLogin().equalsIgnoreCase("newUserLogin")),
                "В БД не обновился логин пользователя");
    }
}
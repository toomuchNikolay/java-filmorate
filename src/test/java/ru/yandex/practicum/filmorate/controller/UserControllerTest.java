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
        controller.getUsers().clear();
        controller.getRegisteredEmail().clear();
        user = new User();
        user.setEmail("TesT@mail.com");
        user.setLogin("login");
        user.setName("test name");
        user.setBirthday(LocalDate.of(2000, 1, 10));
    }

    @Test
    void checkMethodGetAllUsers() {
        controller.add(user);

        User yetUser = new User();
        yetUser.setEmail("somemail@gmail.com");
        yetUser.setLogin("anotherLogin");
        yetUser.setName("my name");
        yetUser.setBirthday(LocalDate.of(2010, 10, 1));
        controller.add(yetUser);

        assertEquals(2, controller.getAllUsers().size(),
                "Ошибка в количестве добавленных пользователей");
        assertTrue(controller.getAllUsers().contains(user),
                "При получении списка пользователей отсутствует user");
        assertTrue(controller.getAllUsers().contains(yetUser),
                "При получении списка пользователей отсутствует yetUser");
    }

    @Test
    void checkMethodAdd() {
        user.setName(" ");
        controller.add(user);

        assertNotNull(user.getId(), "При добавлении пользователя не присвоился id");
        assertTrue(controller.getAllUsers().contains(user), "Пользователь не добавился в БД");
        assertEquals(user.getLogin(), user.getName(), "При пустом имени пользователя не добавился логин");
        assertTrue(controller.getRegisteredEmail().contains(user.getEmail().toLowerCase().trim()),
                "Не добавилась в список используемых указанная электронная почта пользователя");

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
        updateUser.setName(" ");
        controller.update(updateUser);

        assertEquals(1, controller.getAllUsers().size(), "Изменилось количество в БД");

        assertFalse(controller.getAllUsers().stream()
                        .anyMatch(u -> u.getLogin().equalsIgnoreCase("login")),
                "В БД есть пользователь со старым логином");
        assertTrue(controller.getAllUsers().stream()
                        .anyMatch(u -> u.getLogin().equalsIgnoreCase("newUserLogin")),
                "В БД не обновился логин пользователя");
        assertEquals(updateUser.getLogin(), updateUser.getName(),
                "При пустом имени пользователя не добавился логин");
    }

    @Test
    void checkCurrentStatusSetEmail() {
        controller.add(user);

        User yetUser = new User();
        yetUser.setEmail("SomeMail@gmail.com");
        yetUser.setLogin("anotherLogin");
        yetUser.setName("my name");
        yetUser.setBirthday(LocalDate.of(2010, 10, 1));
        controller.add(yetUser);

        assertEquals(2, controller.getRegisteredEmail().size(),
                "Ошибка в количестве используемых электронных почт в хранилище");
        assertTrue(controller.getRegisteredEmail().contains("test@mail.com"),
                "В хранилище используемых электронных почт отсутствует данные пользователя user");
        assertTrue(controller.getRegisteredEmail().contains("somemail@gmail.com"),
                "В хранилище используемых электронных почт отсутствует данные пользователя yetUser");

        User updateUserTrue = new User();
        updateUserTrue.setId(user.getId());
        updateUserTrue.setEmail("updateMail@box.ru");
        updateUserTrue.setLogin(user.getLogin());
        updateUserTrue.setName(user.getName());
        updateUserTrue.setBirthday(user.getBirthday());
        controller.update(updateUserTrue);

        assertEquals(2, controller.getRegisteredEmail().size(),
                "Ошибка в количестве используемых электронных почт в хранилище");
        assertTrue(controller.getRegisteredEmail().contains("updatemail@box.ru"),
                "В хранилище используемых почт не добавилась новая информация о пользователе user");
        assertFalse(controller.getRegisteredEmail().contains("test@mail.com"),
                "В хранилище используемых почт не удалилась старая информация о пользователе user");

        User updateUserFalse = new User();
        updateUserFalse.setId(yetUser.getId());
        updateUserFalse.setEmail("UPDATEmail@box.ru ");
        updateUserFalse.setLogin(yetUser.getLogin());
        updateUserFalse.setName(yetUser.getName());
        updateUserFalse.setBirthday(yetUser.getBirthday());

        DuplicatedDataException e = assertThrows(DuplicatedDataException.class, () -> controller.update(updateUserFalse));
        assertEquals("Пользователь с указанной электронной почтой уже зарегистрирован", e.getMessage(),
                "Не сработало исключение при обновлении пользователя с аналогичной электронной почтой");
        assertTrue(controller.getAllUsers().contains(yetUser),
                "При обновлении пользователя и указании существующей электронной почты данные обновились");
        assertTrue(controller.getRegisteredEmail().contains("somemail@gmail.com"),
                "В хранилище используемых почт изменилась почта при обновлении пользователя с используемой почтой");
    }
}

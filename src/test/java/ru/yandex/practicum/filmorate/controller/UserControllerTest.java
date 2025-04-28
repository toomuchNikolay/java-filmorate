package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController controller;
    @Autowired
    private UserStorage storage;

    private User user;

    @BeforeEach
    void setUp() {
        ((InMemoryUserStorage) storage).getUsers().clear();
        ((InMemoryUserStorage) storage).getUsedEmails().clear();
        user = User.builder()
                .email("testaddress@email.com")
                .login("MyLogin")
                .name("Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
    }

    @Test
    void checkMethodAddUser() {
        ResponseEntity<User> response = controller.addUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "При успешном добавлении пользователя вернулся не 201 статус");
    }

    @Test
    void checkMethodUpdateUser() {
        controller.addUser(user);
        User updatedUser = User.builder()
                .id(user.getId())
                .email("newaddress@mail.com")
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        ResponseEntity<User> response = controller.updateUser(updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "При успешном обновлении пользователя вернулся на 200 статус");
    }

    @Test
    void checkMethodAddFriendAndRemoveFriend() {
        User friend = User.builder()
                .email("MyFriend@mail.ru")
                .login("Friend")
                .name(" ")
                .birthday(LocalDate.of(1999, 1, 19))
                .build();
        controller.addUser(user);
        controller.addUser(friend);
        ResponseEntity<Void> responseAddFriend = controller.addFriend(user.getId(), friend.getId());
        ResponseEntity<Void> responseRemoveFriend = controller.removeFriend(user.getId(), friend.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseAddFriend.getStatusCode(),
                "При успешном добавлении в друзья вернулся не 204 статус");
        assertEquals(HttpStatus.NO_CONTENT, responseRemoveFriend.getStatusCode(),
                "При успешном удалении из друзей вернулся не 204 статус");
    }

    @Test
    void checkGetMethods() {
        User anotherUser = User.builder()
                .email("someaddress@gmail.com")
                .login("Login")
                .name(" ")
                .birthday(LocalDate.of(1991, 11, 1))
                .build();
        controller.addUser(user);
        controller.addUser(anotherUser);

        ResponseEntity<Collection<User>> responseGetAllUsers = controller.getAllUsers();
        ResponseEntity<Collection<User>> responseGetFriends = controller.getFriends(user.getId());
        ResponseEntity<Collection<User>> responseGetCommonFriends = controller.getCommonFriends(user.getId(), anotherUser.getId());

        assertEquals(HttpStatus.OK, responseGetAllUsers.getStatusCode(),
                "При успешном запросе списка всех пользователей вернулся не 200 статус");
        assertEquals(HttpStatus.OK, responseGetFriends.getStatusCode(),
                "При успешном запросе списка друзей пользователя вернулся не 200 статус");
        assertEquals(HttpStatus.OK, responseGetCommonFriends.getStatusCode(),
                "При успешном запросе списка общих друзей пользователей вернулся не 200 статус");
    }
}

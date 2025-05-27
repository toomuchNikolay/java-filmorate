package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class UserControllerTest {
    private final UserController userController;
    private final LikeService likeService;

    @Test
    void addUser() {
        NewUserRequest request = NewUserRequest.builder()
                .email("test@mail.com")
                .login("test")
                .name("")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        UserDto user = userController.addUser(request);
        UserDto findUser = userController.getUsersById(user.getId());

        assertThat(user.getId()).isNotNull();
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void updateUser() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .login("UpdatedLogin")
                .build();
        UserDto user = userController.updateUser(request);
        Collection<UserDto> collection = userController.getAllUsers();

        assertThat(collection)
                .extracting(UserDto::getLogin)
                .contains(user.getLogin());
    }

    @Test
    void getUserLikes() {
        likeService.addLike(4L, 1L);
        likeService.addLike(5L, 1L);

        assertThat(userController.getUserLikes(1L))
                .hasSize(2)
                .extracting(FilmDto::getId)
                .contains(4L, 5L);
    }

    @Test
    void addAndRemoveFriend() {
        userController.addFriend(1L, 2L);
        userController.addFriend(1L, 3L);
        userController.addFriend(3L, 1L);
        userController.addFriend(3L, 2L);

        assertThat(userController.getFriends(1L))
                .hasSize(2)
                .extracting(UserDto::getId)
                .contains(2L, 3L);
        assertThat(userController.getCommonFriends(1L, 3L))
                .extracting(UserDto::getId)
                .contains(2L);

        userController.removeFriend(1L, 3L);

        assertThat(userController.getFriends(1L))
                .hasSize(1)
                .extracting(UserDto::getId)
                .doesNotContain(3L);
    }
}
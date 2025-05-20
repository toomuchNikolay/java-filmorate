package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) user.setEmail(request.getEmail());
        if (request.hasLogin()) user.setLogin(request.getLogin());
        if (request.hasName()) user.setName(request.getName());
        if (request.hasBirthday()) user.setBirthday(request.getBirthday());
        return user;
    }
}

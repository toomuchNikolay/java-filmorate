package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserRequest {
    @NotNull(message = "Идентификатор пользователя должен быть указан")
    private Long id;

    @Email(message = "Электронная почта должна соответствовать формату")
    private String email;

    @NotEmpty(message = "Логин должен быть указан")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем времени")
    private LocalDate birthday;

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}

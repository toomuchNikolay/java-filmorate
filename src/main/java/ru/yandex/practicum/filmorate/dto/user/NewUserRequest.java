package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NewUserRequest {
    @NotEmpty(message = "Электронная почта должна быть указана")
    @Email(message = "Электронная почта должна соответствовать формату")
    private String email;

    @NotEmpty(message = "Логин должен быть указан")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем времени")
    private LocalDate birthday;
}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;

/**
 * User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /** Целочисленный идентификатор пользователя. */
    private Long id;

    /** Электронная почта пользователя. */
    @NotEmpty(groups = ValidationGroups.PostValidationGroup.class, message = "Электронная почта не может быть пустой")
    @Email(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Электронная почта не соответствует формату электронного адреса")
    private String email;

    /** Логин пользователя. */
    @Pattern(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            regexp = "^\\S+$", message = "Логин не может быть пустым или содержать пробелы")
    private String login;

    /** Имя пользователя для отображения. */
    private String name;

    /** Дата рождения пользователя. */
    @PastOrPresent(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}

package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /** Целочисленный идентификатор пользователя. */
    @Null(groups = ValidationGroups.PostValidationGroup.class,
            message = "При добавлении пользователя id не должен быть указан")
    @NotNull(groups = ValidationGroups.PutValidationGroup.class,
            message = "При обновлении пользователя id должен быть указан")
    private Long id;

    /** Электронная почта пользователя. */
    @NotEmpty(groups = ValidationGroups.PostValidationGroup.class, message = "Электронная почта не может быть пустой")
    @Email(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Электронная почта не соответствует формату электронного адреса")
    private String email;

    /** Логин пользователя. */
    @NotEmpty(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Логин не может быть пустым")
    @Pattern(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            regexp = "^\\S*$", message = "Логин не может содержать пробелы")
    private String login;

    /** Имя пользователя для отображения. */
    private String name;

    /** Дата рождения пользователя. */
    @PastOrPresent(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    /** Список друзей пользователя. */
    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Long> friends = new HashSet<>();
}

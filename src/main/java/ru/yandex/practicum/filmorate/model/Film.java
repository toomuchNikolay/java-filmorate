package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValid;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    /** Целочисленный идентификатор фильма. */
    @Null(groups = ValidationGroups.PostValidationGroup.class,
            message = "При добавлении фильма id не должен быть указан")
    @NotNull(groups = ValidationGroups.PutValidationGroup.class,
            message = "При обновлении фильма id должен быть указан")
    private Long id;

    /** Название фильма. */
    @NotBlank(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Название не может быть пустым")
    private String name;

    /** Описание фильма. */
    @Size(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class}, max = 200,
            message = "Описание не может превышать 200 символов")
    private String description;

    /** Дата релиза фильма. */
    @ReleaseDateValid(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Дата релиза должна быть не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    /** Продолжительность фильма. */
    @Positive(groups = {ValidationGroups.PostValidationGroup.class, ValidationGroups.PutValidationGroup.class},
            message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    /** Список пользователей, оценивших фильм. */
    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Long> likes = new HashSet<>();
}

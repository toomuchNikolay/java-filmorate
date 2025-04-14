package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValid;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    /** Целочисленный идентификатор фильма. */
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
}

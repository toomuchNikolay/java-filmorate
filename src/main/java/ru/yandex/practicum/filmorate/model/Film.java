package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValid;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "name" })
public class Film {
    /** Целочисленный идентификатор фильма. */
    private Long id;

    /** Название фильма. */
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    /** Описание фильма. */
    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    /** Дата релиза фильма. */
    @ReleaseDateValid(message = "Дата релиза должна быть не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    /** Продолжительность фильма. */
    @DurationMin(millis = 1, message = "Продолжительность фильма должна быть положительным числом")
    private Duration duration;
}

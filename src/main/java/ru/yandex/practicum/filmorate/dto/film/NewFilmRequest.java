package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValid;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
public class NewFilmRequest {
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @ReleaseDateValid(message = "Дата релиза должна быть не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

//    Не удалось реализовать валидацию через аннотацию, чтобы возвращался 404 для тестов - либо 400, либо 500
//    @MpaValid(message = "Рейтинг должен быть G, PG, PG-13, R или NC-17")
    private MpaDto mpa;

    private Collection<GenreDto> genres;
}

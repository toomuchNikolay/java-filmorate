package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
public class FilmDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaDto mpa;
    private Collection<GenreDto> genres;
}

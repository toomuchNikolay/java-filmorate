package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(MpaMapper.mapToMpa(request.getMpa()))
                .genres(Collections.emptyList())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getFilmId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(MpaMapper.mapToMpaDto(film.getMpa()))
                .genres(Collections.emptyList())
                .build();
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasName()) film.setName(request.getName());
        if (request.hasDescription()) film.setDescription(request.getDescription());
        if (request.hasReleaseDate()) film.setReleaseDate(request.getReleaseDate());
        if (request.hasDuration()) film.setDuration(request.getDuration());
        if (request.hasMpaRating()) film.setMpa(MpaMapper.mapToMpa(request.getMpa()));
        return film;
    }
}

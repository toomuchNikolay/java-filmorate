package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {
    public static Genre mapToGenre(GenreDto genreDto) {
        return Genre.builder()
                .genreId(genreDto.getId())
                .name(genreDto.getName())
                .build();
    }

    public static GenreDto mapToGenreDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
}

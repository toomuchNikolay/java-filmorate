package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmGenre {
    private Long filmId;
    private Long genreId;
}

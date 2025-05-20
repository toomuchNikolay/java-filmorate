package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Long filmId;
    private Long userId;
}

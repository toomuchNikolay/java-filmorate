package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
}

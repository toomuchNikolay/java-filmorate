package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MpaMapper {
    public static Mpa mapToMpa(MpaDto mpaDto) {
        return Mpa.builder()
                .mpaId(mpaDto.getId())
                .name(mpaDto.getName())
                .build();
    }

    public static MpaDto mapToMpaDto(Mpa mpa) {
        return MpaDto.builder()
                .id(mpa.getMpaId())
                .name(mpa.getName())
                .build();
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage storage;

    public Collection<GenreDto> getAllGenres() {
        return storage.findAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenreById(Long genreId) {
        return storage.findGenreById(genreId)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}

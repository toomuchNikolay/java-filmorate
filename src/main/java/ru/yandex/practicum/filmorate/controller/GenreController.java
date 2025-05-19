package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<GenreDto> getAllGenres() {
        log.info("Запрос на получение списка всех жанров фильма");
        Collection<GenreDto> collection = genreService.getAllGenres();
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto getGenreById(@PathVariable Long genreId) {
        log.info("Запрос на получение жанра id = {}", genreId);
        GenreDto findGenre = genreService.getGenreById(genreId);
        log.info("Запрос успешно обработан, получен жанр: {}", findGenre);
        return findGenre;
    }
}

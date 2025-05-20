package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    public FilmDto addFilm(NewFilmRequest film) {
        validateMpa(film.getMpa().getId());
        validateGenre(film.getGenres());
        Film added = filmStorage.addFilm(FilmMapper.mapToFilm(film));
        log.debug("Добавлена запись фильма: {}", added);
        addGenresToFilm(added.getFilmId(), film.getGenres());
        FilmDto dto = FilmMapper.mapToFilmDto(added);
        dto.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        dto.setGenres(getGenresForFilm(added.getFilmId()));
        return dto;
    }

    public FilmDto updateFilm(UpdateFilmRequest film) {
        Film findFilm = filmStorage.findFilmById(film.getId())
                .orElseThrow(() -> {
                    log.error("Попытка обновить несуществующий фильм");
                    return new NotFoundException("Фильм не найден");
                });
        FilmMapper.updateFilmFields(findFilm, film);
        if (film.getGenres() != null) {
            addGenresToFilm(findFilm.getFilmId(), film.getGenres());
        }
        Film updated = filmStorage.updateFilm(findFilm);
        log.debug("Обновлена запись фильма: {}", updated);
        FilmDto dto = FilmMapper.mapToFilmDto(updated);
        dto.setGenres(getGenresForFilm(findFilm.getFilmId()));
        return dto;
    }

    public Collection<FilmDto> getAllFilms() {
        return filmStorage.findAllFilms().stream()
                .map(this::getFilmWithGenres)
                .toList();
    }

    public FilmDto getFilmById(Long filmId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> {
                    log.error("Попытка найти несуществующий фильм");
                    return new NotFoundException("Фильм не найден");
                });
        FilmDto dto = FilmMapper.mapToFilmDto(film);
        dto.setMpa(mpaService.getMpaById(film.getMpa().getMpaId()));
        dto.setGenres(getGenresForFilm(film.getFilmId()));
        return dto;
    }

    private void validateMpa(Long mpaId) {
        mpaService.getMpaById(mpaId);
    }

    private void validateGenre(Collection<GenreDto> genres) {
        Optional.ofNullable(genres)
                .ifPresent(g -> g.forEach(genre -> genreService.getGenreById(genre.getId())));
    }

    private void addGenresToFilm(Long filmId, Collection<GenreDto> genres) {
        Collection<GenreDto> collection = Optional.ofNullable(genres)
                        .orElse(Collections.emptyList());
        if (!collection.isEmpty()) {
            filmGenreStorage.removeFilmGenres(filmId);
            filmGenreStorage.addFilmGenre(filmId, collection.stream()
                    .map(dto -> Genre.builder().genreId(dto.getId()).name(dto.getName()).build())
                    .toList());
        }
    }

    private Collection<GenreDto> getGenresForFilm(Long filmId) {
        return filmGenreStorage.findGenresFilm(filmId).stream()
                .map(genreService::getGenreById)
                .toList();
    }

    private FilmDto getFilmWithGenres(Film film) {
        FilmDto dto = FilmMapper.mapToFilmDto(film);
        dto.setGenres(getGenresForFilm(film.getFilmId()));
        return dto;
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<MpaDto> getAllMpa() {
        log.info("Запрос на получение списка всех возрастных ограничений для фильма");
        Collection<MpaDto> collection = mpaService.getAllMpa();
        log.info("Запрос успешно обработан, размер списка: {}", collection.size());
        return collection;
    }

    @GetMapping("/{mpaId}")
    @ResponseStatus(HttpStatus.OK)
    public MpaDto getMpaById(@PathVariable Long mpaId) {
        log.info("Запрос на получение рейтинга id = {}", mpaId);
        MpaDto findMpa = mpaService.getMpaById(mpaId);
        log.info("Запрос успешно обработан, получен рейтинг: {}", findMpa);
        return findMpa;
    }
}

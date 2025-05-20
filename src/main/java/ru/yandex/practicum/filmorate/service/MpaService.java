package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage storage;

    public Collection<MpaDto> getAllMpa() {
        return storage.findAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    public MpaDto getMpaById(Long mpaId) {
        return storage.findMpaById(mpaId)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA не найден"));
    }
}

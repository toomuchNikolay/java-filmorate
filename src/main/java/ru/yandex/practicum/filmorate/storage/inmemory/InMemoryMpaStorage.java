package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.*;

@Repository
public class InMemoryMpaStorage implements MpaStorage {
    private final Map<Long, Mpa> ratings = Map.of(
            1L, new Mpa(1L, "G"),
            2L, new Mpa(2L, "PG"),
            3L, new Mpa(3L, "PG-13"),
            4L, new Mpa(4L, "R"),
            5L, new Mpa(5L, "NC-17")
    );

    @Override
    public Collection<Mpa> findAllMpa() {
        return ratings.values();
    }

    @Override
    public Optional<Mpa> findMpaById(Long mpaId) {
        return Optional.ofNullable(ratings.get(mpaId));
    }
}

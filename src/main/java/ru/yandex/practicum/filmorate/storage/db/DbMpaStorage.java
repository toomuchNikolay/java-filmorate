package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DbMpaStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
    private static final String FIND_BY_ID = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";

    public DbMpaStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Mpa> findAllMpa() {
        return getMany(FIND_ALL);
    }

    @Override
    public Optional<Mpa> findMpaById(Long mpaId) {
        return getOne(FIND_BY_ID, mpaId);
    }
}

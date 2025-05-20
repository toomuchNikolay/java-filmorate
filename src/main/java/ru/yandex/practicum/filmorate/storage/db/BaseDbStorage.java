package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class))
                .orElseThrow(() -> new InternalServerException("Не удалось сохранить данные"));
    }

    protected void insertWithoutKey(String query, Object... params) {
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps;
        });
    }

    protected void insertCollection(String query, Collection<?> collection, Object... params) {
        jdbc.batchUpdate(query, new BatchPreparedStatementSetter() {
            final List<?> list = new ArrayList<>(collection);
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                for (int j = 0; j < params.length; j++) {
                    ps.setObject(j + 1, params[j]);
                }
                ps.setObject(params.length + 1, list.get(i));
            }

            @Override
            public int getBatchSize() {
                return collection.size();
            }
        });
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

    protected Optional<T> getOne(String query, Object... params) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(query, mapper, params));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> getMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }
}

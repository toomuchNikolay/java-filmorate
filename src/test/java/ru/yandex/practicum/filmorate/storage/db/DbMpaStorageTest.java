package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbMpaStorage.class, MpaRowMapper.class})
class DbMpaStorageTest {
    private final DbMpaStorage storage;

    @Test
    void findAllMpa() {
        Collection<Mpa> collection = storage.findAllMpa();

        assertThat(collection)
                .hasSize(5)
                .extracting(Mpa::getName)
                .contains("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    void findMpaById() {
        Optional<Mpa> findMpa = storage.findMpaById(4L);

        assertThat(findMpa)
                .isPresent()
                .hasValueSatisfying(m -> assertThat(m.getName()).isEqualTo("R"));
    }
}
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class GenreControllerTest {
    private final GenreController genreController;

    @Test
    void get() {
        assertThat(genreController.getAllGenres())
                .hasSize(6);
        assertThat(genreController.getGenreById(4L))
                .extracting(GenreDto::getName)
                .isEqualTo("Триллер");
    }
}
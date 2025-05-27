package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class MpaControllerTest {
    private final MpaController mpaController;

    @Test
    void get() {
        assertThat(mpaController.getAllMpa())
                .hasSize(5);
        assertThat(mpaController.getMpaById(4L))
                .extracting(MpaDto::getName)
                .isEqualTo("R");
    }
}
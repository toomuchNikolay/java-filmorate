package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@email.com");
        user.setLogin("TestLogin");
        user.setName("Test name");
        user.setBirthday(LocalDate.of(2000, 10, 10));
    }

    @Test
    void shouldNotViolationsWhenFieldCorrect() {
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertTrue(violations.isEmpty(), "Ошибка в валидации полей User");
    }

    @Test
    void checkAnnotationFieldEmailWithBlankValue() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertEquals("Электронная почта не соответствует формату электронного адреса",
                violations.iterator().next().getMessage(), "Не прошла проверка на пустую электронную почту");
    }

    @Test
    void checkAnnotationFieldEmailWithBadPattern() {
        user.setEmail("badmail@ru");
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertEquals("Электронная почта не соответствует формату электронного адреса",
                violations.iterator().next().getMessage(), "Не прошла проверка на формат электронной почты");
    }

    @Test
    void checkAnnotationFieldLoginWithBlankValue() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertEquals("Логин не может быть пустым или содержать пробелы", violations.iterator().next().getMessage(),
                "Не прошла проверка на пустой логин");
    }

    @Test
    void checkAnnotationFieldLoginWithSpace() {
        user.setLogin("new login");
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertEquals("Логин не может быть пустым или содержать пробелы", violations.iterator().next().getMessage(),
                "Не прошла проверка логина с пробелом");
    }

    @Test
    void checkAnnotationFieldBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validatorFactoryBean.validate(user);

        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage(),
                "Не прошла проверка даты рождения");
    }
}
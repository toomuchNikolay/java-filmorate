package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.validator.ValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    private Validator validator;
    Set<ConstraintViolation<User>> postViolations;
    Set<ConstraintViolation<User>> putViolations;

    private User postUser;
    private User putUser;

    @BeforeEach
    void setUp() {
        postViolations = new HashSet<>();
        putViolations = new HashSet<>();

        postUser = User.builder()
                .email("First_user@email.com")
                .login("First")
                .name("Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        putUser = User.builder()
                .id(10L)
                .email("Second_user@email.com")
                .login("Second")
                .name("Name")
                .birthday(LocalDate.of(2002, 2, 22))
                .build();
    }

    @Test
    void checkNullAnnotationFieldId() {
        postViolations = validator.validate(putUser, ValidationGroups.PostValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка id при добавлении пользователя");
    }

    @Test
    void checkNotNullAnnotationFieldId() {
        putViolations = validator.validate(postUser, ValidationGroups.PutValidationGroup.class);

        assertFalse(putViolations.isEmpty(), "Не прошла проверка id при обновлении пользователя");
    }

    @Test
    void checkNotEmptyAnnotationFieldEmail() {
        postUser.setEmail("");
        postViolations = validator.validate(postUser, ValidationGroups.PostValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка пустой почты при добавлении пользователя");
    }

    @Test
    void checkEmailAnnotationFieldEmail() {
        postUser.setEmail("bad-mail?ru@");
        putUser.setEmail("@bad-mail?ru");
        postViolations = validator.validate(postUser, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putUser, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(),
                "Не прошла проверка формата электронной почты при добавлении пользователя");
        assertFalse(putViolations.isEmpty(),
                "Не прошла проверка формата электронной почты при обновлении пользователя");
    }

    @Test
    void checkNotEmptyAnnotationFieldLogin() {
        postUser.setLogin("");
        putUser.setLogin("");

        postViolations = validator.validate(postUser, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putUser, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(), "Не прошла проверка на пустой логин при добавлении пользователя");
        assertFalse(putViolations.isEmpty(), "Не прошла проверка на пустой логин при обновлении пользователя");
    }

    @Test
    void checkPatternAnnotationFieldLogin() {
        postUser.setLogin(" ");
        putUser.setLogin("My Login");

        postViolations = validator.validate(postUser, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putUser, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(),
                "Не прошла проверка на логин с пробелами при добавлении пользователя");
        assertFalse(putViolations.isEmpty(),
                "Не прошла проверка на логин с пробелами при обновлении пользователя");
    }

    @Test
    void checkPastOrPresentAnnotationFieldBirthday() {
        postUser.setBirthday(LocalDate.now().plusDays(1));
        putUser.setBirthday(LocalDate.now().plusMonths(1));

        postViolations = validator.validate(postUser, ValidationGroups.PostValidationGroup.class);
        putViolations = validator.validate(putUser, ValidationGroups.PutValidationGroup.class);

        assertFalse(postViolations.isEmpty(),
                "Не прошла проверка на дату рождения в будущем при добавлении пользователя");
        assertFalse(putViolations.isEmpty(),
                "Не прошла проверка на дату рождения в будущем при обновлении пользователя");
    }
}

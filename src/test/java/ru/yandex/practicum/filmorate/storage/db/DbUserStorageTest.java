package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbUserStorage.class, UserRowMapper.class})
class DbUserStorageTest {
    private final DbUserStorage storage;

    private User createUser() {
        return User.builder()
                .email("test@mail.com")
                .login("Login")
                .name("test")
                .birthday(LocalDate.of(2000, 10, 2))
                .build();
    }

    @Test
    void addUser() {
        User added = storage.addUser(createUser());

        assertThat(added.getUserId()).isNotNull();
        assertThat(added.getLogin()).isEqualTo("Login");
    }

    @Test
    void updateUser() {
        User added = storage.addUser(createUser());
        added.setLogin("NewLogin");
        User updated = storage.updateUser(added);

        assertThat(updated.getLogin()).isEqualTo("NewLogin");
    }

    @Test
    void findAllUsers() {
        User first = storage.addUser(createUser());
        Collection<User> collection = storage.findAllUsers();

        assertThat(collection)
                .hasSize(6)
                .extracting(User::getLogin)
                .contains(first.getLogin());
    }

    @Test
    void findUserById() {
        User added = storage.addUser(createUser());
        Long id = added.getUserId();
        Optional<User> findUser = storage.findUserById(id);

        assertThat(findUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u.getUserId()).isEqualTo(id));
    }
}
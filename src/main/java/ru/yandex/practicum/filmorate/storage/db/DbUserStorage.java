package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DbUserStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String INSERT = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    private static final String FIND_ALL = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE user_id = ?";

    public DbUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User addUser(User user) {
        Long id = insert(
                INSERT,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setUserId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getUserId()
        );
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        return getMany(FIND_ALL);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return getOne(FIND_BY_ID, userId);
    }
}

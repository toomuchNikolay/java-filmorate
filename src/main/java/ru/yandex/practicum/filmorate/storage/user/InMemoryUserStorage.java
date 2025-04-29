package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usedEmails = new HashSet<>();

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        usedEmails.add(user.getEmail().toLowerCase().trim());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        String oldEmail = users.get(user.getId()).getEmail().toLowerCase().trim();
        String newEmail = user.getEmail().toLowerCase().trim();
        if (!oldEmail.equals(newEmail)) {
            usedEmails.remove(oldEmail);
            usedEmails.add(newEmail);
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean isUsedMail(String email) {
        return usedEmails.contains(email.toLowerCase().trim());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

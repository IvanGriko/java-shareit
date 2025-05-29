package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserDaoServiceImpl implements UserDaoService {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int usersIdCount = 1;

    @Override
    public User add(User user) {
        checkEmail(user);
        user.setId(usersIdCount);
        users.put(usersIdCount, user);
        emails.add(user.getEmail());
        usersIdCount++;
        return user;
    }

    @Override
    public User update(int id, User user) {
        checkUserInMemory(id);
        updateEmail(findById(id).getEmail(), user.getEmail());
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User findById(int id) {
        checkUserInMemory(id);
        return users.get(id);
    }

    @Override
    public void delete(int id) {
        checkUserInMemory(id);
        emails.remove(findById(id).getEmail());
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private void checkEmail(User user) {
        if (emails.contains(user.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
    }

    private void updateEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
        emails.add(newEmail);
    }

    private void checkUserInMemory(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }
}

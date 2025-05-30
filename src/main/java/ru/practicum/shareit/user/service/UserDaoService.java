package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDaoService {

    User add(User user);

    User update(int id, User user);

    User findById(int id);

    void delete(int id);

    List<User> findAll();

}
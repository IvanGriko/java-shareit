package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    UserDto update(Integer id, UserDto userDto);

    UserDto findById(Integer id);

    void delete(Integer id);

    List<UserDto> findAll();
}
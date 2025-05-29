package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDtoServiceImpl implements UserDtoService {
    private final UserDaoService userDaoService;

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        checkEmail(user);
        userDaoService.add(user);
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
        User user = new User();
        UserDto userFromMemory = findById(id);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        } else {
            user.setName(userFromMemory.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(user);
            user.setEmail(userDto.getEmail());
        } else {
            user.setEmail(userFromMemory.getEmail());
        }
        user.setId(id);
        return UserDtoMapper.toUserDto(userDaoService.update(id, user));
    }

    @Override
    public UserDto findById(int id) {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
        User user = userDaoService.findById(id);
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    public void delete(int id) {
        if (isUserInMemory(id)) {
            userDaoService.delete(id);
        } else {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userDaoService.findAll().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkEmail(User user) {
        boolean isEmailNotUnique = userDaoService.findAll().stream()
                .anyMatch(thisUser -> thisUser.getEmail().equals(user.getEmail())
                        && !(thisUser.getId() == user.getId()));
        if (isEmailNotUnique) {
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
    }

    private boolean isUserInMemory(int userId) {
        return userDaoService.findAll().stream()
                .anyMatch(user -> user.getId() == userId);
    }
}

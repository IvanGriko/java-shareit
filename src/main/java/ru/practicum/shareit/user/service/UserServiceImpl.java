package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        user = userRepository.save(user);
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(int id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                            return new NotFoundException("Пользователя с " + id + " не существует");
                        }
                );
        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto findById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                            return new NotFoundException("Пользователя с " + id + " не существует");
                        }
                );
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }
}

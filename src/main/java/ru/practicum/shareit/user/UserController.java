package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.markers.Create;
import ru.practicum.shareit.user.markers.Update;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto add(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("POST-запрос на добавление пользователя {}", userDto);
        return userService.add(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable int userId) {
        log.info("GET-запрос на получение пользователя id = {}", userId);
        return userService.findById(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("GET-запрос на получение списка всех пользователей");
        return userService.findAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable int userId, @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("PATCH-запрос на обновление пользователя id = {}", userId);
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        log.info("DELETE-запрос на удаление пользователя id = {}", userId);
        userService.delete(userId);
    }

}

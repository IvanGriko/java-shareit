package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserDtoServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserDtoServiceImpl userDtoService;

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userDtoService.add(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable int userId) {
        return userDtoService.findById(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userDtoService.findAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable int userId, @RequestBody UserDto userDto) {
        return userDtoService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        userDtoService.delete(userId);
    }

}

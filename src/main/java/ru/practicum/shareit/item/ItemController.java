package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemDtoServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemDtoServiceImpl itemDtoService;

    @PostMapping
    public ItemDto add(@RequestHeader(USER_HEADER) int userId,
                       @Valid @RequestBody ItemDto itemDto) {
        try {
            log.info("POST Запрос на добавление пользователем с id = " + userId + " предмета " + itemDto.toString());
            return itemDtoService.add(userId, itemDto);
        } catch (NotFoundException e) {
            log.error("Пользователь с id = " + userId + " не найден", e);
            throw e; // Исключение будет обработано и возвращен код 404
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_HEADER) int userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable int itemId) {
        log.info("PATCH Запрос на обновление предмета с id = " + itemId + " пользователем с id = " + userId);
        return itemDtoService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(USER_HEADER) int userId,
                            @PathVariable("itemId")
                            int itemId) {
        log.info("GET Запрос на получение предмета с id = " + itemId + " пользователем с id = " + userId);
        return itemDtoService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(USER_HEADER) int userId) {
        log.info("GET Запрос на получение предметов пользователя с id = " + userId);
        return itemDtoService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(USER_HEADER) int userId,
                                     @RequestParam(name = "text") String text) {
        log.info("GET Запрос на поиск предметов");
        return itemDtoService.search(userId, text);
    }
}
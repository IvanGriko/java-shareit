package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemServiceImpl itemDtoService;

    @PostMapping
    public ItemDtoOut add(@RequestHeader(USER_HEADER) int userId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("POST-запрос на добавление пользователем с id {} вещи с id {}", userId, itemDto.toString());
        return itemDtoService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut update(@RequestHeader(USER_HEADER) int userId,
                             @RequestBody ItemDto itemDto,
                             @PathVariable int itemId) {
        log.info("PATCH-запрос на обновление вещи с id {} пользователем с id {} ", itemId, userId);
        return itemDtoService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut findById(@RequestHeader(USER_HEADER) int userId,
                               @PathVariable("itemId")
                               int itemId) {
        log.info("GET-запрос на получение вещи с id {} пользователем с id {} ", itemId, userId);
        return itemDtoService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> findAll(@RequestHeader(USER_HEADER) int userId) {
        log.info("GET-запрос на получение списка вещей пользователя с id {}", userId);
        return itemDtoService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItems(@RequestHeader(USER_HEADER) int userId,
                                        @RequestParam(name = "text") String text) {
        log.info("GET-запрос на поиск вещей c текстом '{}'", text);
        return itemDtoService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_HEADER) int userId,
                                       @Validated @RequestBody CommentDto commentDto,
                                       @PathVariable int itemId) {
        log.info("POST-запрос на создание комментария с id {}", itemId);
        return itemDtoService.createComment(userId, commentDto, itemId);
    }

}
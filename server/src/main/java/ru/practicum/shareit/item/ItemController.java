package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_HEADER;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDtoOut add(@RequestHeader(USER_HEADER) Integer userId,
                          @RequestBody ItemDto itemDto) {
        log.info("POST-запрос на добавление пользователем {} предмета {}", userId, itemDto.toString());
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut update(@RequestHeader(USER_HEADER) Integer userId,
                             @RequestBody ItemDto itemDto,
                             @PathVariable Integer itemId) {
        log.info("PATCH-запрос на обновление предмета {} пользователем {} ", itemId, userId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut findById(@RequestHeader(USER_HEADER) Integer userId,
                               @PathVariable("itemId") Integer itemId) {
        log.info("GET-запрос на получение предмета {} пользователем {} ", itemId, userId);
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> findAll(@RequestHeader(USER_HEADER) Integer userId,
                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET-запрос на получение предметов пользователя {}", userId);
        return itemService.findAll(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItems(@RequestHeader(USER_HEADER) Integer userId,
                                        @RequestParam(name = "text") String text,
                                        @RequestParam(value = "from", defaultValue = "0") Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET-запрос на поиск предметов c текстом: {}", text);
        return itemService.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_HEADER) Integer userId,
                                       @Validated @RequestBody CommentDto commentDto,
                                       @PathVariable Integer itemId) {
        log.info("POST-запрос на создание комментария {}", itemId);
        return itemService.createComment(userId, commentDto, itemId);
    }
}
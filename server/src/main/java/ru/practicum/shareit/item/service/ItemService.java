package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {

    ItemDtoOut add(Integer userId, ItemDto itemDto);

    ItemDtoOut update(Integer userId, Integer itemId, ItemDto itemDto);

    ItemDtoOut findItemById(Integer userId, Integer itemId);

    List<ItemDtoOut> findAll(Integer userId, Integer from, Integer size);

    List<ItemDtoOut> search(Integer userId, String text, Integer from, Integer size);

    CommentDtoOut createComment(Integer userId, CommentDto commentDto, Integer itemId);
}
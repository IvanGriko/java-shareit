package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {

    ItemDtoOut add(int userId, ItemDto itemDto);

    ItemDtoOut update(int userId, int itemId, ItemDto itemDto);

    ItemDtoOut findItemById(int userId, int itemId);

    List<ItemDtoOut> findAll(int userId);

    List<ItemDtoOut> search(int userId, String text);

    CommentDtoOut createComment(int userId, CommentDto commentDto, int itemId);

}

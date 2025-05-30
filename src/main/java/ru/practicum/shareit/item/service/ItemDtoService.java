package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemDtoService {

    ItemDto add(int userId, ItemDto itemDto);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    ItemDto findItemById(int userId, int itemId);

    List<ItemDto> findAll(int userId);

    List<ItemDto> search(int userId, String text);
}

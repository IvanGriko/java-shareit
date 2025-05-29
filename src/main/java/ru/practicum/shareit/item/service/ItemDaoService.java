package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDaoService {
    Item add(Item item);

    Item update(Item item);

    Optional<Item> findItemById(int itemId);

    List<Item> findAll(int userId);

    List<Item> search(String text);
}

package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDaoServiceImpl implements ItemDaoService {

    private final Map<Integer, List<Item>> itemsMap = new HashMap<>();
    private int itemsIdCount = 1;

    @Override
    public Item add(Item item) {
        item.setId(itemsIdCount);
        List<Item> listItems = itemsMap.get(item.getOwner());
        if (listItems == null) {
            listItems = new ArrayList<>();
            itemsMap.put(item.getOwner(), listItems);
        }
        listItems.add(item);
        itemsIdCount++;
        return item;
    }

    @Override
    public Item update(Item item) {
        List<Item> userItems = itemsMap.get(item.getOwner());
        List<Item> toRemove = userItems.stream()
                .filter(userItem -> (userItem.getId())==(item.getId()))
                .toList();
        userItems.removeAll(toRemove);
        userItems.add(item);
        return item;
    }

    @Override
    public Optional<Item> findItemById(int itemId) {
        return itemsMap.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> (item.getId())==(itemId))
                .findFirst();
    }

    @Override
    public List<Item> findAll(int userId) {
        return new ArrayList<>(itemsMap.get(userId));
    }

    @Override
    public List<Item> search(String text) {
        String searchText = text.toLowerCase();
        return itemsMap.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
}

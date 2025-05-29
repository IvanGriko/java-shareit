package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != 0 ? item.getRequest() : 0);
    }

    public static Item toItem(ItemDto itemDto) {
        Boolean available = itemDto.getAvailable() != null ? itemDto.getAvailable() : false;
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                available,
                itemDto.getRequest() != 0 ? itemDto.getRequest() : 0);
    }
}

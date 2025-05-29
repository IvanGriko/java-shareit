package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.service.UserDaoService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemDtoServiceImpl implements ItemDtoService {
    private final ItemDaoService itemDaoService;
    private final UserDaoService userDaoService;

    @Override
    public ItemDto add(int userId, ItemDto itemDto) {
        UserDto user = UserDtoMapper.toUserDto(userDaoService.findById(userId));
        Item item = ItemDtoMapper.toItem(itemDto);
        item.setOwner((UserDtoMapper.toUser(user)).getId());
        return ItemDtoMapper.toItemDto(itemDaoService.add(item));
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        UserDto user = UserDtoMapper.toUserDto(userDaoService.findById(userId));
        Optional<Item> itemOptional = itemDaoService.findItemById(itemId);
        if (itemOptional.isPresent()) {
            if (!(itemOptional.get().getOwner()==userId)) {
                throw new NotFoundException(String.format("Пользователь с id %s " +
                        "не является владельцем вещи id %s.", userId, itemId));
            }
            Item itemFromStorage = itemOptional.get();
            Item item = ItemDtoMapper.toItem(itemDto);
            if (Objects.isNull(item.getAvailable())) {
                item.setAvailable(itemFromStorage.getAvailable());
            }
            if (Objects.isNull(item.getDescription())) {
                item.setDescription(itemFromStorage.getDescription());
            }
            if (Objects.isNull(item.getName())) {
                item.setName(itemFromStorage.getName());
            }
            item.setId(itemFromStorage.getId());
            item.setRequest(itemFromStorage.getRequest());
            item.setOwner(itemFromStorage.getOwner());
            return ItemDtoMapper.toItemDto(itemDaoService.update(item));
        }
        return itemDto;
    }

    @Override
    public ItemDto findItemById(int userId, int itemId) {
        userDaoService.findById(userId);
        Optional<Item> itemGet = itemDaoService.findItemById(itemId);
        if (itemGet.isEmpty()) {
            throw new NotFoundException(String.format("У пользователя с id %s не " +
                    "существует вещи с id %s", userId, itemId));
        }
        return ItemDtoMapper.toItemDto(itemGet.get());
    }

    @Override
    public List<ItemDto> findAll(int userId) {
        userDaoService.findById(userId);
        List<Item> itemList = itemDaoService.findAll(userId);
        return itemList.stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(int userId, String text) {
        userDaoService.findById(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> itemList = itemDaoService.search(text);
        return itemList.stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

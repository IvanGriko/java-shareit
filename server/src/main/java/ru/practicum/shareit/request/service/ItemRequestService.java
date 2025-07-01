package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoOut add(Integer userId, ItemRequestDto requestDto);

    List<ItemRequestDtoOut> getUserRequests(Integer userId);

    List<ItemRequestDtoOut> getAllRequests(Integer userId, Integer from, Integer size);

    ItemRequestDtoOut getRequestById(Integer userId, Integer requestId);
}
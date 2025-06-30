package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    UserService userService;
    ItemRequestRepository requestRepository;

    @Override
    @Transactional
    public ItemRequestDtoOut add(Integer userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.findById(userId));
        ItemRequest request = ItemRequestMapper.toRequest(user, itemRequestDto);
        request.setRequester(user);
        return ItemRequestMapper.toRequestDtoOut(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoOut> getUserRequests(Integer userId) {
        UserMapper.toUser(userService.findById(userId));
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequesterId(userId);
        return itemRequestList.stream()
                .map(ItemRequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoOut> getAllRequests(Integer userId, Integer from, Integer size) {
        UserMapper.toUser(userService.findById(userId));
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequester_IdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        return itemRequestList.stream()
                .map(ItemRequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOut getRequestById(Integer userId, Integer requestId) {
        userService.findById(userId);
        Optional<ItemRequest> requestById = requestRepository.findById(requestId);
        if (requestById.isEmpty()) {
            throw new NotFoundException(String.format("Запрос с id %s не был найден.", requestId));
        }
        return ItemRequestMapper.toRequestDtoOut(requestById.get());
    }
}
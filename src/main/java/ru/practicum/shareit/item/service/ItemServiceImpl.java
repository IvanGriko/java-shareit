package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    CommentRepository commentRepository;
    BookingRepository bookingRepository;
    UserService userService;

    @Override
    @Transactional
    public ItemDtoOut add(int userId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Item item = ItemDtoMapper.toItem(itemDto);
        item.setOwner(UserDtoMapper.toUser(user));
        return ItemDtoMapper.toItemDtoOut(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDtoOut update(int userId, int itemId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id " + itemId + " не существует")
                );
        if (!UserDtoMapper.toUser(user).equals(item.getOwner())) {
            throw new NotFoundException("Пользователь с id " + userId + "не является владельцем вещи с id " + itemId);
        }
        Boolean isAvailable = itemDto.getAvailable();
        if (isAvailable != null) {
            item.setAvailable(isAvailable);
        }
        String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        return ItemDtoMapper.toItemDtoOut(item);
    }

    @Override
    @Transactional
    public ItemDtoOut findItemById(int userId, int itemId) {
        userService.findById(userId);
        Optional<Item> itemGet = itemRepository.findById(itemId);
        if (itemGet.isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userId + " не существует вещи с id " + itemId);
        }
        Item item = itemGet.get();
        ItemDtoOut itemDtoOut = ItemDtoMapper.toItemDtoOut(itemGet.get());
        itemDtoOut.setComments(getAllItemComments(itemId));
        if (!(item.getOwner().getId() == userId)) {
            return itemDtoOut;
        }
        List<Booking> bookings = bookingRepository.findAllByItemAndStatusOrderByStartAsc(item, BookingStatus.APPROVED);
        List<BookingDtoOut> bookingDTOList = bookings
                .stream()
                .map(BookingDtoMapper::toBookingOut)
                .collect(toList());
        itemDtoOut.setLastBooking(getLastBooking(bookingDTOList, LocalDateTime.now()));
        itemDtoOut.setNextBooking(getNextBooking(bookingDTOList, LocalDateTime.now()));
        return itemDtoOut;
    }

    @Override
    @Transactional
    public List<ItemDtoOut> findAll(int userId) {
        UserDto owner = userService.findById(userId);
        List<Item> itemList = itemRepository.findAllByOwnerId(userId);
        List<Integer> idList = itemList.stream()
                .map(Item::getId)
                .collect(toList()).reversed();
        Map<Integer, List<CommentDtoOut>> comments = commentRepository.findAllByItemIdIn(idList)
                .stream()
                .map(CommentDtoMapper::toCommentDtoOut)
                .collect(groupingBy(CommentDtoOut::getItemId, toList()));
        Map<Integer, List<BookingDtoOut>> bookings = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(itemList,
                        BookingStatus.APPROVED)
                .stream()
                .map(BookingDtoMapper::toBookingOut)
                .collect(groupingBy(BookingDtoOut::getItemId, toList()));
        return itemList
                .stream()
                .map(item -> ItemDtoMapper.toItemDtoOut(
                        item,
                        getLastBooking(bookings.get(item.getId()), LocalDateTime.now()),
                        comments.get(item.getId()),
                        getNextBooking(bookings.get(item.getId()), LocalDateTime.now())
                ))
                .collect(toList());
    }

    @Override
    @Transactional
    public List<ItemDtoOut> search(int userId, String text) {
        userService.findById(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> itemList = itemRepository.search(text);
        return itemList.stream()
                .map(ItemDtoMapper::toItemDtoOut)
                .collect(toList());
    }

    @Override
    @Transactional
    public CommentDtoOut createComment(int userId, CommentDto commentDto, int itemId) {
        User user = UserDtoMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(itemId);
        if (itemById.isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userId + " не существует вещи с id " + itemId);
        }
        Item item = itemById.get();
        List<Booking> userBookings = bookingRepository.findAllByUserBookings(userId, itemId, LocalDateTime.now());
        if (userBookings.isEmpty()) {
            throw new ValidationException("У пользователя с id " + userId +
                    " должно быть хотя бы одно бронирование вещи с id " + itemId);
        }
        return CommentDtoMapper.toCommentDtoOut(commentRepository.save(CommentDtoMapper.toComment(commentDto, item, user)));
    }

    public List<CommentDtoOut> getAllItemComments(int itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return comments.stream()
                .map(CommentDtoMapper::toCommentDtoOut)
                .collect(toList());
    }

    private BookingDtoOut getLastBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings
                .stream()
                .filter(bookingDTO -> !bookingDTO.getStart().isAfter(time))
                .reduce((booking1, booking2) -> booking1.getStart().isAfter(booking2.getStart()) ? booking1 : booking2)
                .orElse(null);
    }

    private BookingDtoOut getNextBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings
                .stream()
                .filter(bookingDTO -> bookingDTO.getStart().isAfter(time))
                .findFirst()
                .orElse(null);
    }

}

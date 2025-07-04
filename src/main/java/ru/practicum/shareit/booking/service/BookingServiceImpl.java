package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    UserService userService;
    ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoOut add(int userId, BookingDto bookingDto) {
        User user = UserDtoMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(bookingDto.getItemId());
        if (itemById.isEmpty()) {
            throw new NotFoundException("Вещь не найдена.");
        }
        Item item = itemById.get();
        bookingValidation(bookingDto, user, item);
        Booking booking = BookingDtoMapper.toBooking(user, item, bookingDto);
        return BookingDtoMapper.toBookingOut(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut update(int userId, int bookingId, Boolean approved) {
        Booking booking = validateBookingDetails(userId, bookingId, 1);
        assert booking != null;
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        return BookingDtoMapper.toBookingOut(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut findBookingByUserId(int userId, int bookingId) {
        Booking booking = validateBookingDetails(userId, bookingId, 2);
        assert booking != null;
        return BookingDtoMapper.toBookingOut(booking);
    }

    @Override
    @Transactional
    public List<BookingDtoOut> findAll(int bookerId, String state) {
        userService.findById(bookerId);
        switch (validState(state)) {
            case BookingState.ALL:
                return bookingRepository.findAllBookingsByBookerId(bookerId)
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.CURRENT:
                return bookingRepository.findAllCurrentBookingsByBookerId(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.PAST:
                return bookingRepository.findAllPastBookingsByBookerId(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.FUTURE:
                return bookingRepository.findAllFutureBookingsByBookerId(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.WAITING:
                return bookingRepository.findAllWaitingBookingsByBookerId(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.REJECTED:
                return bookingRepository.findAllRejectedBookingsByBookerId(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Неизвестный статус бронирования.");
        }
    }

    @Override
    @Transactional
    public List<BookingDtoOut> findAllOwner(int ownerId, String state) {
        userService.findById(ownerId);
        switch (validState(state)) {
            case BookingState.ALL:
                return bookingRepository.findAllBookingsByOwnerId(ownerId)
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.CURRENT:
                return bookingRepository.findAllCurrentBookingsByOwnerId(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.PAST:
                return bookingRepository.findAllPastBookingsByOwnerId(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.FUTURE:
                return bookingRepository.findAllFutureBookingsByOwnerId(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.WAITING:
                return bookingRepository.findAllWaitingBookingsByOwnerId(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            case BookingState.REJECTED:
                return bookingRepository.findAllRejectedBookingsByOwnerId(ownerId)
                        .stream()
                        .map(BookingDtoMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Неизвестный статус бронирования.");
        }
    }

    private void bookingValidation(BookingDto bookingDto, User user, Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступена для бронирования.");
        }
        if (user.getId() == item.getOwner().getId()) {
            throw new NotFoundException("Вещь не найдена.");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала.");
        }
    }

    private BookingState validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (state == null) {
            throw new IllegalArgumentException("Неизвестный статус бронирования: " + bookingState);
        }
        return state;
    }

    @SneakyThrows
    private Booking validateBookingDetails(int userId, int bookingId, Integer number) {
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if (bookingById.isEmpty()) {
            throw new NotFoundException("Бронь не найдена.");
        }
        Booking booking = bookingById.get();
        switch (number) {
            case 1:
                if (!(booking.getItem().getOwner().getId() == userId)) {
                    throw new BadRequestException("Пользователь не является владельцем вещи.");
                }
                if (!booking.getStatus().equals(BookingStatus.WAITING)) {
                    throw new ValidationException("Бронь cо статусом WAITING.");
                }
                return booking;
            case 2:
                if (!(booking.getBooker().getId() == userId)
                        && !(booking.getItem().getOwner().getId() == userId)) {
                    throw new NotFoundException("Пользователь не владелeц и не автор бронирования.");
                }
                return booking;
        }
        return null;
    }

}
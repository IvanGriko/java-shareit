package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
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
    public BookingDtoOut add(Integer userId, BookingDto bookingDto) {
        User user = UserMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(bookingDto.getItemId());
        if (itemById.isEmpty()) {
            throw new NotFoundException("Вещь не найдена.");
        }
        Item item = itemById.get();
        bookingValidation(bookingDto, user, item);
        Booking booking = BookingMapper.toBooking(user, item, bookingDto);
        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut update(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = validateBookingDetails(userId, bookingId, 1);
        assert booking != null;
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut findBookingByUserId(Integer userId, Integer bookingId) {
        Booking booking = validateBookingDetails(userId, bookingId, 2);
        assert booking != null;
        return BookingMapper.toBookingOut(booking);
    }

    @Override
    @Transactional
    public List<BookingDtoOut> findAll(Integer bookerId, String bookingState, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        userService.findById(bookerId);
        switch (validState(bookingState)) {
            case ALL:
                return bookingRepository.findAllBookingsByBookerId(bookerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllCurrentBookingsByBookerId(bookerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllPastBookingsByBookerId(bookerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllFutureBookingsByBookerId(bookerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllWaitingBookingsByBookerId(bookerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllRejectedBookingsByBookerId(bookerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Неподдерживаемый статус");
        }
    }

    @Override
    @Transactional
    public List<BookingDtoOut> findAllOwner(Integer ownerId, String bookingState, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        userService.findById(ownerId);
        switch (validState(bookingState)) {
            case ALL:
                return bookingRepository.findAllBookingsByOwnerId(ownerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllCurrentBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllPastBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllFutureBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllWaitingBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllRejectedBookingsByOwnerId(ownerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Неподдерживаемый статус");
        }
    }

    private void bookingValidation(BookingDto bookingDto, User user, Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования.");
        }
        if (user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Вещь не найдена.");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }
    }

    private BookingState validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (state == null) {
            throw new IllegalArgumentException("Неизвестный статус: " + bookingState);
        }
        return state;
    }

    private Booking validateBookingDetails(Integer userId, Integer bookingId, Integer number) {
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if (bookingById.isEmpty()) {
            throw new NotFoundException("Бронь не найдена.");
        }
        Booking booking = bookingById.get();
        switch (number) {
            case 1:
                if (!booking.getItem().getOwner().getId().equals(userId)) {
                    throw new NotFoundException("Пользователь не является владельцем");
                }
                if (!booking.getStatus().equals(BookingStatus.WAITING)) {
                    throw new ValidationException("Бронь не cо статусом WAITING");
                }
                return booking;
            case 2:
                if (!booking.getBooker().getId().equals(userId)
                        && !booking.getItem().getOwner().getId().equals(userId)) {
                    throw new NotFoundException("Пользователь не владелец и не автор бронирования ");
                }
                return booking;
        }
        return null;
    }
}
package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut create(@RequestHeader(USER_HEADER) int userId,
                                @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST-запрос на создание нового бронирования от пользователя c id: {} ", userId);
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(USER_HEADER) int userId,
                                      @PathVariable("bookingId")
                                      int bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH-запрос на обновление статуса бронирования от владельца с id: {}", userId);
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut findBookingById(@RequestHeader(USER_HEADER) int userId,
                                         @PathVariable("bookingId")
                                         int bookingId) {
        log.info("GET-запрос на получение данных о бронировании от пользователя с id: {}", userId);
        return bookingService.findBookingByUserId(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(USER_HEADER) int userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        log.info("GET-запрос на получение списка всех бронирований пользователя с id: {} и статусом {}", userId, bookingState);
        return bookingService.findAll(userId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(USER_HEADER) int ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        log.info("GET-запрос на получение списка всех бронирований владельца с id: {} и статусом {}", ownerId, bookingState);
        return bookingService.findAllOwner(ownerId, bookingState);
    }

}
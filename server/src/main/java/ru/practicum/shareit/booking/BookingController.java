package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut create(@RequestHeader(USER_HEADER) Integer userId,
                                @RequestBody BookingDto bookingDto) {
        log.info("POST-запрос на создание нового бронирования вещи от пользователя {} ", userId);
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(USER_HEADER) Integer userId,
                                      @PathVariable("bookingId")
                                      Integer bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH-запрос на обновление статуса бронирования вещи от владельца {}", userId);
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut findBookingById(@RequestHeader(USER_HEADER) Integer userId,
                                         @PathVariable("bookingId")
                                         Integer bookingId) {
        log.info("GET-запрос на получение данных о бронировании от пользователя {}", userId);
        return bookingService.findBookingByUserId(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(USER_HEADER) Integer userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET-запрос на получение списка всех бронирований текущего пользователя {} со статусом {}", userId, bookingState);
        return bookingService.findAll(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(USER_HEADER) Integer ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET-запрос на получение списка всех бронирований текущего владельца {} со статусом {}", ownerId, bookingState);
        return bookingService.findAllOwner(ownerId, bookingState, from, size);
    }
}
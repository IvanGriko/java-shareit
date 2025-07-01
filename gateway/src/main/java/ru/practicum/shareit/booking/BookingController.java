package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.user.markers.Create;

import static ru.practicum.shareit.Constants.USER_HEADER;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_HEADER) int userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Неизвестный " +
                "статус бронирования: " + stateParam));
        log.info("GET-запрос бронирования с state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_HEADER) int userId,
                                           @Validated(Create.class) @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("POST-запрос создания бронирования с id={} пользователя с id={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) int userId,
                                             @PathVariable Integer bookingId) {
        log.info("GET-запрос бронировния {} пользователя с id={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwner(@RequestHeader(USER_HEADER) Integer ownerId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                              @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный статус бронирования: " + bookingState));
        log.info("GET-запрос на получение списка всех бронирований c state {}, userId={}, from={}, size={}", bookingState,
                ownerId, from, size);
        return bookingClient.getAllOwner(ownerId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(USER_HEADER) Integer userId,
                                               @PathVariable("bookingId") Integer bookingId,
                                               @RequestParam("approved") Boolean approved) {
        log.info("PATCH-запрос на обновление статуса бронирования {} от владельца с id={}", bookingId, userId);
        return bookingClient.update(userId, bookingId, approved);
    }
}
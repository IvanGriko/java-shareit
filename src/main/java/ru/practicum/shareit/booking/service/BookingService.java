package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut add(int userId, BookingDto bookingDto);

    BookingDtoOut update(int userId, int bookingId, Boolean approved);

    BookingDtoOut findBookingByUserId(int userId, int bookingId);

    List<BookingDtoOut> findAll(int userId, String state);

    List<BookingDtoOut> findAllOwner(int userId, String state);
}
package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingDtoMapper {

    public Booking toBooking(User user, Item item, BookingDto bookingDto) {
        return new Booking(
                item,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                user,
                BookingStatus.WAITING);
    }

    public BookingDtoOut toBookingOut(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                ItemDtoMapper.toItemDtoOut(booking.getItem()),
                booking.getStart(),
                booking.getEnd(),
                UserDtoMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public static BookingItemDto toBookingItemDto(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getBooker().getId());
    }

}
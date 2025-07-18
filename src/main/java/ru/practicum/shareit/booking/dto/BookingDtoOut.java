package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoOut {

    int id;
    ItemDtoOut item;
    LocalDateTime start;
    LocalDateTime end;
    UserDto booker;
    BookingStatus status;

    public int getItemId() {
        return item.getId();
    }

    public int getBookerId() {
        return booker.getId();
    }

}
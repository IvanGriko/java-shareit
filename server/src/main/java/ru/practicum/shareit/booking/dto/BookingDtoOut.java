package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoOut {

    Integer id;
    ItemDtoOut item;
    LocalDateTime start;
    LocalDateTime end;
    UserDto booker;
    BookingStatus status;

    public Integer getItemId() {
        return item.getId();
    }

    public int getBookerId() {
        return booker.getId();
    }
}
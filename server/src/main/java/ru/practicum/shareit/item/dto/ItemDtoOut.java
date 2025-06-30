package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOut {

    Integer id;
    String name;
    String description;
    Boolean available;
    BookingDtoOut lastBooking;
    List<CommentDtoOut> comments;
    BookingDtoOut nextBooking;
    Integer requestId;

    public ItemDtoOut(Integer id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDtoOut(Integer id, String name, String description, Boolean available, BookingDtoOut lastBooking,
                      List<CommentDtoOut> comments, BookingDtoOut nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.comments = comments;
        this.nextBooking = nextBooking;
    }
}
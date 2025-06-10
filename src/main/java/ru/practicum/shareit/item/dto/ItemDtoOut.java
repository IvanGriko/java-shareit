package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoOut {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoOut lastBooking;
    private List<CommentDtoOut> comments;
    private BookingDtoOut nextBooking;

    public ItemDtoOut(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
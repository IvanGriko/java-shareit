package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    int id;
    @Size(max = 255)
    @NotBlank
    String name;
    @Size(max = 512)
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    int request;

    public ItemDto(int id, String name, String description, Boolean available, int request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }

    public ItemDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto(String name, String description, boolean available) {
    }

}

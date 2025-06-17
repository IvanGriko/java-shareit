package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {

    int id;
    @Size(max = 512)
    String description;
    UserDto requester;
    LocalDateTime created;

    public ItemRequestDto(int id, String description) {
        this.id = id;
        this.description = description;
    }

}
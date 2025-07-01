package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.valid.StartBeforeEndDateValid;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEndDateValid
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookItemRequestDto {

    @NotNull
    int itemId;
    @NotNull
    LocalDateTime start;
    @NotNull
    LocalDateTime end;
}

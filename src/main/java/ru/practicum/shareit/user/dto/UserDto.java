package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.markers.Create;
import ru.practicum.shareit.user.markers.Update;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    int id;
    @NotBlank(groups = {Create.class})
    String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    String email;

}

package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100)
    @NotBlank(groups = {Create.class})
    String name;
    @Size(max = 100)
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    String email;

}

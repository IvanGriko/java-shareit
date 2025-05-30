package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    int id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;

    public UserDto(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}

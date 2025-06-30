package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTest
class BookingDtoTest {

    @Autowired
    JacksonTester<BookingDto> json;
    static final String DATE_TIME = "2023-07-23T07:33:00";
    BookingDto bookingDto = null;

    @BeforeEach
    public void init() {
        bookingDto = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.parse("2023-07-23T07:33:00"))
                .end(LocalDateTime.parse("2023-07-23T07:33:00"))
                .build();
    }

    @Test
    @SneakyThrows
    public void startSerializes() {
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.start")
                .isEqualTo(DATE_TIME);
    }

    @Test
    @SneakyThrows
    public void endSerializes() {
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.end")
                .isEqualTo(DATE_TIME);
    }
}
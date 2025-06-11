package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    Item item;
    @Column(name = "start_date", nullable = false)
    LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    @ToString.Exclude
    User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    BookingStatus status;

    public Booking(Item item, LocalDateTime start, LocalDateTime end, User booker, BookingStatus status) {
        this.item = item;
        this.start = start;
        this.end = end;
        this.booker = booker;
        this.status = status;
    }

}
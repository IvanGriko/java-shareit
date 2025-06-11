package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "description", nullable = false, length = 255)
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @ToString.Exclude
    User requester;
    @PastOrPresent
    LocalDateTime created;

    public ItemRequest(int id, String description) {
        this.id = id;
        this.description = description;
    }

}
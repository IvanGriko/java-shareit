package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "text", nullable = false)
    String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    User author;
    @Column(name = "created")
    @CreationTimestamp
    LocalDateTime created;

    public Comment(String text, Item item, User author) {
        this.text = text;
        this.item = item;
        this.author = author;
    }

}
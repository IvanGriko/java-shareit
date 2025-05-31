package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Item {

    int id;
    String name;
    String description;
    boolean available;
    int owner;
    int request;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Item(int id, String name, String description, boolean available, int owner, int request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public Item(int id, String name, String description, Boolean available, Integer request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }

    public boolean getAvailable() {
        return available;
    }

}

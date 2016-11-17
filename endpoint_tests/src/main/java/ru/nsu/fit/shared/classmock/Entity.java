package ru.nsu.fit.shared.classmock;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Entity<T> {
    @JsonProperty("data")
    public T data;

    public Entity(T data) {
        this.data = data;
    }
    public Entity() {}
}

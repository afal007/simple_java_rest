package ru.nsu.fit.endpoint.service.database.data;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Entity<T> {
    private T data;

    public Entity(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

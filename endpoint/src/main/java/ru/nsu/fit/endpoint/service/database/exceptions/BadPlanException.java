package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadPlanException extends Exception{
    public BadPlanException() { super(); };
    public BadPlanException(String message) { super(message); };
}

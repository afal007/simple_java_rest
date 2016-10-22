package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanException extends Exception{
    public BadServicePlanException() { super(); };
    public BadServicePlanException(String message) { super(message); };
}

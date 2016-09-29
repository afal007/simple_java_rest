package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanDetailsException extends BadServicePlanException {
    public static final String LONG_DETAILS_MESSAGE = "Details shouldn't be longer than 1024 symbols.";
    public static final String SHORT_DETAILS_MESSAGE = "Name should be at least 1 symbol.";

    public BadServicePlanDetailsException() { super(); }
    public BadServicePlanDetailsException(String message) { super(message); }
}

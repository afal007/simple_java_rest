package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadPlanDetailsException extends BadPlanException {
    public static final String LONG_DETAILS_MESSAGE = "Details shouldn't be longer than 1024 symbols.";
    public static final String SHORT_DETAILS_MESSAGE = "Name should be at least 1 symbol.";

    public BadPlanDetailsException() { super(); }
    public BadPlanDetailsException(String message) { super(message); }
}

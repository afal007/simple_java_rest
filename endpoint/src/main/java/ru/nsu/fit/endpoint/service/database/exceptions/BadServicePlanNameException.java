package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanNameException extends BadServicePlanException {
    public static final String WRONG_SYMBOLS_MESSAGE = "Name shouldn't contain special symbols.";
    public static final String LONG_NAME_MESSAGE = "Name shouldn't be longer than 128 symbols.";
    public static final String SHORT_NAME_MESSAGE = "Name should be at least 2 symbols.";

    public BadServicePlanNameException() { super(); }
    public BadServicePlanNameException(String message) { super(message); }
}
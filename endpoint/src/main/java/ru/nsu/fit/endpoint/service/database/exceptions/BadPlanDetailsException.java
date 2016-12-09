package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadPlanDetailsException extends BadPlanException {
    public static final String LONG_DETAILS_MESSAGE = "Details shouldn't be longer than 1024 symbols.";
    public static final String SHORT_DETAILS_MESSAGE = "Name should be at least 1 symbol.";

    public BadPlanDetailsException() { super(); }
    public BadPlanDetailsException(String message) { super(message); }
}

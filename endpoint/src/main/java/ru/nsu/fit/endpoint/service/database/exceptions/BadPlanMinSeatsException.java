package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadPlanMinSeatsException extends BadPlanException{
    public static final String BIG_MINSEATS_MESSAGE = "Min seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MINSEATS_MESSAGE = "Min seats shouldn't be less than 1.";

    public BadPlanMinSeatsException() { super(); }
    public BadPlanMinSeatsException(String message) { super(message); }
}

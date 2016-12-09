package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadPlanMaxSeatsException extends BadPlanException {
    public static final String BIG_MAXSEATS_MESSAGE = "Max seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MAXSEATS_MESSAGE = "Max seats shouldn't be less than 1.";
    public static final String LESS_THAN_MINSEATS_MESSAGE = "Max seats shouldn't be less than min seats.";

    public BadPlanMaxSeatsException() { super(); }
    public BadPlanMaxSeatsException(String message) { super(message); }
}

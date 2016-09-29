package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanMaxSeatsException extends BadServicePlanException {
    public static final String BIG_MAXSEATS_MESSAGE = "Max seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MAXSEATS_MESSAGE = "Max seats shouldn't be less than 1.";

    public BadServicePlanMaxSeatsException() { super(); }
    public BadServicePlanMaxSeatsException(String message) { super(message); }
}

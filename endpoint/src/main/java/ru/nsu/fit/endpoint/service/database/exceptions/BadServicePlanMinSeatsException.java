package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanMinSeatsException extends BadServicePlanException{
    public static final String BIG_MINSEATS_MESSAGE = "Min seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MINSEATS_MESSAGE = "Min seats shouldn't be less than 1.";
    public static final String GREATER_THAN_MAXSEATS_MESSAGE = "Min seats shouldn't be greater than max seats.";

    public BadServicePlanMinSeatsException() { super(); }
    public BadServicePlanMinSeatsException(String message) { super(message); }
}

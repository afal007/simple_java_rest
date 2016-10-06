package ru.nsu.fit.endpoint.service.database.exceptions;

public class BadSubscriptionException extends Exception{
    public BadSubscriptionException() { super(); }
    public BadSubscriptionException(String msg) { super(msg); }
    
    public static final String BIG_MAXSEATS_MESSAGE = "Max seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MAXSEATS_MESSAGE = "Max seats shouldn't be less than 1.";
    public static final String MAXSEATS_LESS_THAN_MINSEATS_MESSAGE = "Max seats shouldn't be less than min seats.";
    public static final String MAXSEATS_LESS_THAN_USEDSEATS_MESSAGE = "Max seats shouldn't be less than used seats.";
    
    public static final String BIG_MINSEATS_MESSAGE = "Min seats shouldn't be bigger than 9999999.";
    public static final String SMALL_MINSEATS_MESSAGE = "Min seats shouldn't be less than 1.";
    public static final String MINSEATS_GREATER_THAN_USEDSEATS_MESSAGE = "used seats shouldn't be less than min seats.";
}

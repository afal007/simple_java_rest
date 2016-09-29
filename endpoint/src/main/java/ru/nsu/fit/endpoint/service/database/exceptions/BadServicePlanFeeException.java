package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadServicePlanFeeException extends BadServicePlanException{
    public static final String NEGATIVE_FEE_MESSAGE = "Fee can't be less than 0.";
    public static final String BIG_FEE_MESSAGE = "Fee can't be greater than 999999.";

    public BadServicePlanFeeException() { super(); }
    public BadServicePlanFeeException(String message) { super(message); }
}

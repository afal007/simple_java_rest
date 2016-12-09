package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadPlanFeeException extends BadPlanException{
    public static final String NEGATIVE_FEE_MESSAGE = "Fee can't be less than 0.";
    public static final String BIG_FEE_MESSAGE = "Fee can't be greater than 999999.";

    public BadPlanFeeException() { super(); }
    public BadPlanFeeException(String message) { super(message); }
}

package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadCustomerLastNameException extends BadCustomerException {
    public static final String WRONG_SYMBOLS_MESSAGE = "Last name should begin with upper case letter" +
            " and shouldn't contain any symbols or digits or other uppercase letters.";
    public static final String SHORT_LASTNAME_MESSAGE = "Last name should be at least 2 symbols.";
    public static final String LONG_LASTNAME_MESSAGE = "Last name shouldn't be longer than 12 symbols.";

    public BadCustomerLastNameException() { super(); }
    public BadCustomerLastNameException(String msg) { super(msg); }
}
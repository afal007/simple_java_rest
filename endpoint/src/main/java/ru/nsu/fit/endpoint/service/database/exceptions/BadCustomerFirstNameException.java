package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadCustomerFirstNameException extends BadCustomerException {
    public static final String WRONG_SYMBOLS_MESSAGE = "First name should begin with upper case letter" +
            " and shouldn't contain any symbols or digits or other uppercase letters.";
    public static final String SHORT_FIRSTNAME_MESSAGE = "First name should be at least 2 symbols.";
    public static final String LONG_FIRSTNAME_MESSAGE = "First name shouldn't be longer than 12 symbols.";

    public BadCustomerFirstNameException() { super(); }
    public BadCustomerFirstNameException(String msg) { super(msg); }
}

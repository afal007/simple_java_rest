package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadCustomerPasswordException extends BadCustomerException {
    public static final String EMPTY_PASSWORD_MESSAGE = "Password mustn't be empty.";
    public static final String SHORT_PASSWORD_MESSAGE = "Password should be at least 6 symbols.";
    public static final String LONG_PASSWORD_MESSAGE = "Password shouldn't be longer than 12 symbols.";
    public static final String EASY_PASSWORD_MESSAGE  = "Password is too easy.";
    public static final String PASSWORD_CONTAINS_LOGIN_MESSAGE  = "Password can't contain login.";
    public static final String PASSWORD_CONTAINS_FIRSTNAME_MESSAGE  = "Password can't contain first name.";
    public static final String PASSWORD_CONTAINS_LASTNAME_MESSAGE  = "Password can't contain last name.";

    public BadCustomerPasswordException(String msg) {
        super(msg);
    }

    public BadCustomerPasswordException() {
        super();
    }
}

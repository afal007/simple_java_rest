package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadCustomerLoginException extends BadCustomerException {
    public static final String INVALID_LOGIN_MESSAGE = "Login should be valid email.";

    public BadCustomerLoginException() { super(); }
    public BadCustomerLoginException(String msg) { super(msg); }
}

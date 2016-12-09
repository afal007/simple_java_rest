package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@SuppressWarnings("SameParameterValue")
public class BadCustomerLoginException extends BadCustomerException {
    public static final String INVALID_LOGIN_MESSAGE = "Login should be valid email.";

    public BadCustomerLoginException() { super(); }
    public BadCustomerLoginException(String msg) { super(msg); }
}

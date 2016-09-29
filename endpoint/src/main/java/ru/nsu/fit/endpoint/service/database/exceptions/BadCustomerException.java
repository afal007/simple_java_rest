package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class BadCustomerException extends Exception {
    public BadCustomerException() { super(); }
    public BadCustomerException(String msg) { super(msg); }
}
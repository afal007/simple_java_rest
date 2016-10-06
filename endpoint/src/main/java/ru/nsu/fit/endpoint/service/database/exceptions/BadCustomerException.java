package ru.nsu.fit.endpoint.service.database.exceptions;

public class BadCustomerException extends Exception {
    public BadCustomerException() { super(); }
    public BadCustomerException(String msg) { super(msg); }
}
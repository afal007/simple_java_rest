package ru.nsu.fit.endpoint.service.database.exceptions;

public class BadCustomerAccountException extends BadCustomerException {
    public BadCustomerAccountException() { super(); }
    public BadCustomerAccountException(String msg) { super(msg); }
    
    public static final String NEGATIVE_ACCOUNT = "customer can't have negative balance!";
}

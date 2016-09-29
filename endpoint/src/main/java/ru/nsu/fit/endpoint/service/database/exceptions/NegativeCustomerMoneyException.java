package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * Created by Alex on 29.09.2016.
 */
public class NegativeCustomerMoneyException extends BadCustomerException {
    public static final String NEGATIVE_MONEY_MESSAGE = "Money can't be negative.";

    public NegativeCustomerMoneyException() {super();}
    public NegativeCustomerMoneyException(String message) {super(message);}
}

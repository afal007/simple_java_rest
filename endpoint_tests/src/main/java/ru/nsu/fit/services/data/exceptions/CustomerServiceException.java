package ru.nsu.fit.services.data.exceptions;

import ru.nsu.fit.services.data.CustomerService;
import ru.nsu.fit.shared.classmock.Customer;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class CustomerServiceException extends Exception {
    public CustomerServiceException() { super(); }
    public CustomerServiceException(String msg) { super(msg); }
}

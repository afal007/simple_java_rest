package ru.nsu.fit.endpoint.service.database.exceptions;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BadPlanException extends Exception{
    public BadPlanException() { super(); }

    public BadPlanException(String message) { super(message); }
}

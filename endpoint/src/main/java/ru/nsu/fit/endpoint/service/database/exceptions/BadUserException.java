package ru.nsu.fit.endpoint.service.database.exceptions;

public class BadUserException extends Exception {
    public BadUserException() { super(); }
    public BadUserException(String msg) { super(msg); }
    
    public static final String FIRSTNAME_LENGHT = "first name lenght must be from 2 to 12 letters";
    public static final String FIRSTNAME_INVALID_CHARS = "first name must contain only letters";
    public static final String FIRSTNAME_CAPITAL = "first name must start with uppercase letter and continue with lowercase letters";
    
    public static final String LASTNAME_LENGHT = "last name lenght must be from 2 to 12 letters";
    public static final String LASTNAME_INVALID_CHARS = "last name must contain only letters";
    public static final String LASTNAME_CAPITAL = "last name must start with uppercase letter and continue with lowercase letters";
  
    public static final String EMAIL_INVALID = "invalid login (e-mail)";
    
    public static final String PASSWORD_EMPTY = "password must not be empty";
    public static final String PASSWORD_LENGHT = "password lenght mus be from 6 to 12 symbols";
    public static final String PASSWORD_SIMPLE = "password is too simple";
    public static final String PASSWORD_CONTAINS_CREDENTIALS = "password must not contain user's first name, second name or login(email)";
}
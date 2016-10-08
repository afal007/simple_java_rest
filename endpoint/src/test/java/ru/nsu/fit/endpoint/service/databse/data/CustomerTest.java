package ru.nsu.fit.endpoint.service.databse.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.exceptions.*;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class CustomerTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void FirstNameLongEnough() throws BadCustomerException{
        new Customer("Jo", "Wick", "john_wick@gmail.com", "Strongpass1", 0); //min len
        new Customer("Johnjohnjohn", "Wick", "john_wick@gmail.com", "Strongpass1", 0); //max len
    }

    @Test
    public void LastNameLongEnough() throws BadCustomerException{
        new Customer("Jo", "Wi", "john_wick@gmail.com", "Strongpass1", 0); //min len
        new Customer("Johnjohnjohn", "Wickwickwick", "john_wick@gmail.com", "Strongpass1", 0); //max len
    }

    @Test
    public void FirstNameShort() throws BadCustomerException{
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.SHORT_FIRSTNAME_MESSAGE);
        new Customer("J", "Wick", "john_wick@gmail.com", "Strongpass1", 0);
    }
    @Test
    public void FirstNameLong() throws BadCustomerException{
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.LONG_FIRSTNAME_MESSAGE);
        new Customer("Johnnnnnnnnnn", "Wick", "john_wick@gmail.com", "Strongpass1", 0); //max len + 1
    }

    @Test
    public void LastNameShort() throws BadCustomerException{
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.SHORT_LASTNAME_MESSAGE);
        new Customer("John", "W", "john_wick@gmail.com", "Strongpass1", 0);
    }
    @Test
    public void LastNameLong() throws BadCustomerException{
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.LONG_LASTNAME_MESSAGE);
        new Customer("John", "Wickkkkkkkkkk", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void FirstNameFirstSymUppercase() throws BadCustomerException{
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("hohn", "Wick", "john_wick@gmail.com", "Strongpass1", 0);
    }
    @Test
    public void FirstNameNonFirstSymUppercase() throws BadCustomerException{
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("JohN", "Wick", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void LastNameFirstSymUppercase() throws BadCustomerException{
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("John", "wick", "john_wick@gmail.com", "Strongpass1", 0);
    }
    @Test
    public void LastNameNonFirstSymUppercase() throws BadCustomerException{
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("John", "WicK", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void FirstNameWrongSymbols() throws BadCustomerException{
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("@ohn", "Wick", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void LastNameWrongSymbols() throws BadCustomerException{
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.WRONG_SYMBOLS_MESSAGE);
        new Customer("John", "!ick", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void LoginIncorrect() throws BadCustomerException{
        expectedEx.expect(BadCustomerLoginException.class);
        expectedEx.expectMessage(BadCustomerLoginException.INVALID_LOGIN_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.", "Strongpass1", 0);
    }

    @Test
    public void testCreateNewCustomer() throws BadCustomerException{
        new Customer("John", "Wick", "john_wick@gmail.com", "Strongpass1", 0);
    }

    @Test
    public void PasswordLongEnough() throws  BadCustomerException{
        new Customer("John", "Wick", "john_wick@gmail.com", "Strongpass21", 0); //max len
        new Customer("John", "Wick", "john_wick@gmail.com", "Strpa2", 0); // min len
    }

    @Test
    public void PasswordEmpty() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.EMPTY_PASSWORD_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "", 0);
    }
    @Test
    public void PasswordShort() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.SHORT_PASSWORD_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "123", 0);
    }

    @Test
    public void PasswordLong() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.LONG_PASSWORD_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "123qwe123qwe1", 0);
    }

    @Test
    public void PasswordContainsFirstName() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_FIRSTNAME_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "johnA2C", 0);
    }

    @Test
    public void PasswordContainsLastName() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_LASTNAME_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "wickA2C", 0);
    }

    @Test
    public void PasswordContainsLogin() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_LOGIN_MESSAGE);
        new Customer("John", "Wick", "a@e1.com", "a@e1.com", 0);
    }

    @Test
    public void PasswordEasy() throws BadCustomerException{
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.EASY_PASSWORD_MESSAGE);
        new Customer("John", "Wick", "john_wick@gmail.com", "123qwe", 0);
    }
    
    @Test
    public void MoneyNegative() throws BadCustomerException{
        expectedEx.expect(BadCustomerAccountException.class);
        expectedEx.expectMessage(BadCustomerAccountException.NEGATIVE_ACCOUNT);
        new Customer("John", "Wick", "john_wick@gmail.com", "wickA2C", -1);
    }
}

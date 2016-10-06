package ru.nsu.fit.endpoint.service.databse.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.*;

public class UserTest {
	@Rule
    public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
    public void FirstNameLongEnough() throws BadUserException{
        new User("Jo", "Wick", "john_wick@gmail.com", "Strongpass1", User.UserRole.USER); //min len
        new User("Johnjohnjohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER); //max len
    }

    @Test
    public void LastNameLongEnough() throws BadUserException{
        new User("Jo", "Wi", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER); //min len
        new User("Johnjohnjohn", "Wickwickwick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER); //max len
    }

    @Test
    public void FirstNameShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_LENGHT);
        new User("J", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }
    @Test
    public void FirstNameLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_LENGHT);
        new User("Johnnnnnnnnnn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER); //max len + 1
    }

    @Test
    public void LastNameShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_LENGHT);
        new User("John", "W", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }
    @Test
    public void LastNameLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_LENGHT);
        new User("John", "Wickkkkkkkkkk", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void FirstNameFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_CAPITAL);
        new User("hohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }
    @Test
    public void FirstNameNonFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_CAPITAL);
        new User("JohN", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void LastNameFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_CAPITAL);
        new User("John", "wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }
    @Test
    public void LastNameNonFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_CAPITAL);
        new User("John", "WicK", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void FirstNameWrongSymbols() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_INVALID_CHARS);
        new User("@ohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void LastNameWrongSymbols() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_INVALID_CHARS);
        new User("John", "!ick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void LoginIncorrect() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.EMAIL_INVALID);
        new User("John", "Wick", "john_wick@gmail.", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void testCreateNewUser() throws BadUserException{
        new User("John", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserRole.USER);
    }

    @Test
    public void PasswordLongEnough() throws  BadUserException{
        new User("John", "Wick", "john_wick@gmail.com", "Strongpass21",  User.UserRole.USER); //max len
        new User("John", "Wick", "john_wick@gmail.com", "Strpa2",  User.UserRole.USER); // min len
    }

    @Test
    public void PasswordEmpty() throws BadUserException {
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_EMPTY);
        new User("John", "Wick", "john_wick@gmail.com", "",  User.UserRole.USER);
    }
    @Test
    public void PasswordShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_LENGHT);
        new User("John", "Wick", "john_wick@gmail.com", "123",  User.UserRole.USER);
    }

    @Test
    public void PasswordLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_LENGHT);
        new User("John", "Wick", "john_wick@gmail.com", "123qwe123qwe1",  User.UserRole.USER);
    }

    @Test
    public void PasswordContainsFirstName() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User("John", "Wick", "john_wick@gmail.com", "johnA2C",  User.UserRole.USER);
    }

    @Test
    public void PasswordContainsLastName() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User("John", "Wick", "john_wick@gmail.com", "wickA2C",  User.UserRole.USER);
    }

    @Test
    public void PasswordContainsLogin() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User("John", "Wick", "a@e1.com", "a@e1.com",  User.UserRole.USER);
    }

    @Test
    public void PasswordEasy() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_SIMPLE);
        new User("John", "Wick", "john_wick@gmail.com", "123qwe",  User.UserRole.USER);
    }
}

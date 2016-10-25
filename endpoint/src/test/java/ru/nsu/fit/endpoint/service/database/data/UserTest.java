package ru.nsu.fit.endpoint.service.database.data;

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
        new User.UserData("Jo", "Wick", "john_wick@gmail.com", "Strongpass1", User.UserData.UserRole.USER); //min len
        new User.UserData("Johnjohnjohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER); //max len
    }

    @Test
    public void LastNameLongEnough() throws BadUserException{
        new User.UserData("Jo", "Wi", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER); //min len
        new User.UserData("Johnjohnjohn", "Wickwickwick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER); //max len
    }

    @Test
    public void FirstNameShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_LENGHT);
        new User.UserData("J", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }
    @Test
    public void FirstNameLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_LENGHT);
        new User.UserData("Johnnnnnnnnnn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER); //max len + 1
    }

    @Test
    public void LastNameShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_LENGHT);
        new User.UserData("John", "W", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }
    @Test
    public void LastNameLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_LENGHT);
        new User.UserData("John", "Wickkkkkkkkkk", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void FirstNameFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_CAPITAL);
        new User.UserData("hohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }
    @Test
    public void FirstNameNonFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_CAPITAL);
        new User.UserData("JohN", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void LastNameFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_CAPITAL);
        new User.UserData("John", "wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }
    @Test
    public void LastNameNonFirstSymUppercase() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_CAPITAL);
        new User.UserData("John", "WicK", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void FirstNameWrongSymbols() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.FIRSTNAME_INVALID_CHARS);
        new User.UserData("@ohn", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void LastNameWrongSymbols() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.LASTNAME_INVALID_CHARS);
        new User.UserData("John", "!ick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void LoginIncorrect() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.EMAIL_INVALID);
        new User.UserData("John", "Wick", "john_wick@gmail.", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void testCreateNewUser() throws BadUserException{
        new User.UserData("John", "Wick", "john_wick@gmail.com", "Strongpass1",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordLongEnough() throws  BadUserException{
        new User.UserData("John", "Wick", "john_wick@gmail.com", "Strongpass21",  User.UserData.UserRole.USER); //max len
        new User.UserData("John", "Wick", "john_wick@gmail.com", "Strpa2",  User.UserData.UserRole.USER); // min len
    }

    @Test
    public void PasswordEmpty() throws BadUserException {
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_EMPTY);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "",  User.UserData.UserRole.USER);
    }
    @Test
    public void PasswordShort() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_LENGHT);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "123",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordLong() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_LENGHT);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "123qwe123qwe1",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordContainsFirstName() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "johnA2C",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordContainsLastName() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "wickA2C",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordContainsLogin() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        new User.UserData("John", "Wick", "a@e1.com", "a@e1.com",  User.UserData.UserRole.USER);
    }

    @Test
    public void PasswordEasy() throws BadUserException{
        expectedEx.expect(BadUserException.class);
        expectedEx.expectMessage(BadUserException.PASSWORD_SIMPLE);
        new User.UserData("John", "Wick", "john_wick@gmail.com", "123qwe",  User.UserData.UserRole.USER);
    }
}

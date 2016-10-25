package ru.nsu.fit.endpoint.service.database.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.fit.endpoint.service.database.exceptions.*;
import ru.nsu.fit.endpoint.service.database.data.Customer;

/**
 * Feature: тестирование создания объекта Customer.
 * @author Alexander Fal(falalexandr007@gmail.com)
 */
public class CustomerTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Все поля заполнены корректно.
     * 1. Создать Customer с корректными аргументами
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomer() throws BadCustomerException {
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Пароль пустой.
     * 1.Создать Customer с пустым паролем
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithEmptyPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.EMPTY_PASSWORD_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "", 0);
    }

    /**
     * Пароль слишком короткий.
     * 1.Создать Customer с паролем из 5 символов
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithShortBadPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.SHORT_PASSWORD_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "A2c@d", 0);
    }

    @Test
    public void testCreateNewCustomerWithShortGoodPass() throws BadCustomerException {
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "A2c@dw", 0);
    }

    /**
     * Пароль слишком длинный.
     * 1.Создать Customer с паролем из 13 символов
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithLongBadPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.LONG_PASSWORD_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123QWE123qwe1", 0);
    }

    @Test
    public void testCreateNewCustomerWithLongGoodPass() throws BadCustomerException {
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123QWE123qwe", 0);
    }

    /**
     * Пароль слишком простой.
     * 1.Создать Customer с паролем из 6 символов, не включающего специальные символы и прописные буквы
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithEasyPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.EASY_PASSWORD_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123qwe", 0);

    }

    /**
     * Пароль содержит логин.
     * 1.Создать Customer с паролем, содержащим логин
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithLoginInPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_LOGIN_MESSAGE);

        new Customer.CustomerData("John", "Wick", "a@ya.ru", "Aa@ya.ruS1", 0);
    }

    /**
     * Пароль содержит имя.
     * 1.Создать Customer с паролем из 10 символов, содержащим имя
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithFirstNameInPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_FIRSTNAME_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "ADSJohn123", 0);
    }

    /**
     * Пароль содержит фамилию.
     * 1.Создать Customer с паролем тз 10 символов, содержащим фамилию
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithLastNameInPass() throws BadCustomerException {
        expectedEx.expect(BadCustomerPasswordException.class);
        expectedEx.expectMessage(BadCustomerPasswordException.PASSWORD_CONTAINS_LASTNAME_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "As2WickQwe", 0);
    }

    /**
     * Отрицательное значение счета.
     * 1.Создать Customer со счетом равным -1
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithNegativeMoney() throws BadCustomerException {
        expectedEx.expect(BadCustomerAccountException.class);
        expectedEx.expectMessage(BadCustomerAccountException.NEGATIVE_ACCOUNT);

        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "Strpass123", -1);
    }

    @Test
    public void testCreateNewCustomerWithSmallPositiveMoney() throws BadCustomerException {
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "Strpass123", 1);
    }

    /**
     * Некорректный логин.
     * 1.Создать Customer c некорректным e-mail в поле login
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithInvalidEmailWithoutDomain() throws BadCustomerException {
        expectedEx.expect(BadCustomerLoginException.class);
        expectedEx.expectMessage(BadCustomerLoginException.INVALID_LOGIN_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick", "Strpass123", 0);
    }

    @Test
    public void testCreateNewCustomerWithInvalidEmail() throws BadCustomerException {
        expectedEx.expect(BadCustomerLoginException.class);
        expectedEx.expectMessage(BadCustomerLoginException.INVALID_LOGIN_MESSAGE);

        new Customer.CustomerData("John", "Wick", "john_wick@@gmail.com", "Strpass123", 0);
    }

    /**
     * Имя содержит некорректные символы.
     * 1.Создать Customer с именем из 5 символов, содержащим цифру
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithWrongSymbolsInFirstName() throws BadCustomerException {
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.WRONG_SYMBOLS_MESSAGE);

        new Customer.CustomerData("JoH1n", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Имя слишком короткое.
     * 1.Создать Customer с именем из 1 символа
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithShortBadFirstName() throws BadCustomerException {
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.SHORT_FIRSTNAME_MESSAGE);

        new Customer.CustomerData("J", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    @Test
    public void testCreateNewCustomerWithShortGoodFirstName() throws BadCustomerException {
        new Customer.CustomerData("Jo", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Имя слишком длинное.
     * 1.Создать Customer с именем из 13 символов
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithLongBadFirstName() throws BadCustomerException {
        expectedEx.expect(BadCustomerFirstNameException.class);
        expectedEx.expectMessage(BadCustomerFirstNameException.LONG_FIRSTNAME_MESSAGE);

        new Customer.CustomerData("Johnjohnjohnj", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    @Test
    public void testCreateNewCustomerWithLongGoodFirstName() throws BadCustomerException {
        new Customer.CustomerData("Johnjohnjohn", "Wick", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Фамилия содержит некорректные символы.
     * 1.Создать Customer с фамилией из 5 символов, содержащим цифру
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithWrongSymbolsInLastName() throws BadCustomerException {
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.WRONG_SYMBOLS_MESSAGE);

        new Customer.CustomerData("John", "Wi1ck", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Фамилия слишком короткая.
     * 1.Создать Customer с фамилией из 1 символа
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithShortBadLastName() throws BadCustomerException {
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.SHORT_LASTNAME_MESSAGE);

        new Customer.CustomerData("John", "W", "john_wick@gmail.com", "Strpass123", 0);
    }

    @Test
    public void testCreateNewCustomerWithShortGoodLastName() throws BadCustomerException {
        new Customer.CustomerData("John", "Wi", "john_wick@gmail.com", "Strpass123", 0);
    }

    /**
     * Фамилия слишком длинная.
     * 1.Создать Customer с фамилией из 13 символов
     *  a) Проверить наличие исключения и корректного сообщения
     * @throws BadCustomerException
     */
    @Test
    public void testCreateNewCustomerWithLongBadLastName() throws BadCustomerException {
        expectedEx.expect(BadCustomerLastNameException.class);
        expectedEx.expectMessage(BadCustomerLastNameException.LONG_LASTNAME_MESSAGE);

        new Customer.CustomerData("John", "Wickwickwickw", "john_wick@gmail.com", "Strpass123", 0);
    }

    @Test
    public void testCreateNewCustomerWithLongGoodLastName() throws BadCustomerException {
        new Customer.CustomerData("John", "Wickwickwick", "john_wick@gmail.com", "Strpass123", 0);
    }
}



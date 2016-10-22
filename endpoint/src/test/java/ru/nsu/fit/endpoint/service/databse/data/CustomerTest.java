package ru.nsu.fit.endpoint.service.databse.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.fit.endpoint.service.database.data.Customer;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class CustomerTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testCreateNewCustomer() {
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "strongpass", 0);
    }

    @Test
    public void testCreateNewCustomerWithShortPass() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Password's length should be more or equal 6 symbols");
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123", 0);
    }

    @Test
    public void testCreateNewCustomerWithLongPass() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Password's length should be more or equal 6 symbols");
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123qwe123qwe1", 0);
    }

    @Test
    public void testCreateNewCustomerWithEasyPass() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Password is easy");
        new Customer.CustomerData("John", "Wick", "john_wick@gmail.com", "123qwe", 0);
    }
}

package ru.nsu.fit.endpoint.service.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.Validate;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Customer extends Entity<Customer.CustomerData>  {
    private UUID id;

    public Customer(CustomerData data, UUID id) {
        super(data);
        this.id = id;
        data.validate();
    }

    public UUID getId() {
        return id;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomerData {
        /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
        @JsonProperty("firstName")
        private String firstName;

        /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
        @JsonProperty("lastName")
        private String lastName;

        /* указывается в виде email, проверить email на корректность */
        @JsonProperty("login")
        private String login;

        /* длина от 6 до 12 символов включительно, недолжен быть простым, не должен содержать части login, firstName, lastName */
        @JsonProperty("pass")
        private String pass;

        /* счет не может быть отрицательным */
        @JsonProperty("money")
        private int money;

        private CustomerData() {}

        public CustomerData(String firstName, String lastName, String login, String pass, int money) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
            validate(firstName, lastName, login, pass, money);
        }

        public void validate() {
            validate(firstName, lastName, login, pass, money);
        }

        public static void validate(String firstName, String lastName, String login, String pass, int money) {
            Validate.notNull(pass);
            Validate.isTrue(pass.length() >= 6 && pass.length() < 13, "Password's length should be more or equal 6 symbols and less or equal 12 symbols");
            Validate.isTrue(!pass.equalsIgnoreCase("123qwe"), "Password is easy");
            Validate.isTrue(money >= 0, "Money must be positive value");
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getLogin() {
            return login;
        }

        public String getPass() {
            return pass;
        }

        public int getMoney() {
            return money;
        }

        @Override
        public String toString() {
            return "CustomerData{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", login='" + login + '\'' +
                    ", pass='" + pass + '\'' +
                    ", money=" + money +
                    '}';
        }
    }
}

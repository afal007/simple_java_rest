package ru.nsu.fit.endpoint.service.database.data;

import org.apache.commons.validator.routines.EmailValidator;
import ru.nsu.fit.endpoint.service.database.exceptions.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 */
public class Customer extends Entity<Customer.CustomerData> {

    @JsonProperty("id")
    private UUID id;

    public Customer(CustomerData data, UUID id) throws BadCustomerException {
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

        public CustomerData(String firstName, String lastName, String login, String pass, int money) throws BadCustomerException {
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
            validate(firstName, lastName, login, pass, money);
        }

        void validate() throws BadCustomerException {
            validate(firstName, lastName, login, pass, money);
        }

        private void validate(String firstName, String lastName, String login, String pass, int money) throws BadCustomerException {
            validatePassword(pass);
            validateLogin(login);
            validateFirstName(firstName);
            validateLastName(lastName);
            validateMoney(money);
        }

        private void validatePassword(String pass) throws BadCustomerPasswordException {
            if(pass == null || pass.equals("")) throw new BadCustomerPasswordException( BadCustomerPasswordException.EMPTY_PASSWORD_MESSAGE );

            if(pass.length() < 6) throw new BadCustomerPasswordException( BadCustomerPasswordException.SHORT_PASSWORD_MESSAGE );
            if(pass.length() > 12) throw new BadCustomerPasswordException( BadCustomerPasswordException.LONG_PASSWORD_MESSAGE );

            if(evaluatePassword(pass) <= 50) throw new BadCustomerPasswordException( BadCustomerPasswordException.EASY_PASSWORD_MESSAGE );

            if(pass.contains(login)) throw new BadCustomerPasswordException( BadCustomerPasswordException.PASSWORD_CONTAINS_LOGIN_MESSAGE );
            if(pass.contains(firstName)) throw new BadCustomerPasswordException( BadCustomerPasswordException.PASSWORD_CONTAINS_FIRSTNAME_MESSAGE );
            if(pass.contains(lastName)) throw new BadCustomerPasswordException( BadCustomerPasswordException.PASSWORD_CONTAINS_LASTNAME_MESSAGE );
        }

        private void validateLogin(String login) throws BadCustomerLoginException {
            if(!EmailValidator.getInstance().isValid(login)) { throw new BadCustomerLoginException(BadCustomerLoginException.INVALID_LOGIN_MESSAGE); }
        }

        private void validateFirstName(String firstName) throws BadCustomerFirstNameException {
            if(!firstName.matches("^[A-Z][a-z]*")) { throw new BadCustomerFirstNameException( BadCustomerFirstNameException.WRONG_SYMBOLS_MESSAGE); }

            if(firstName.length() < 2) { throw new BadCustomerFirstNameException( BadCustomerFirstNameException.SHORT_FIRSTNAME_MESSAGE); }
            if(firstName.length() > 12) { throw new BadCustomerFirstNameException( BadCustomerFirstNameException.LONG_FIRSTNAME_MESSAGE); }
        }

        private void validateLastName(String lastName) throws BadCustomerLastNameException {
            if(!lastName.matches("^[A-Z][a-z]*")) { throw new BadCustomerLastNameException( BadCustomerLastNameException.WRONG_SYMBOLS_MESSAGE); }

            if(lastName.length() < 2) { throw new BadCustomerLastNameException( BadCustomerLastNameException.SHORT_LASTNAME_MESSAGE); }
            if(lastName.length() > 12) { throw new BadCustomerLastNameException( BadCustomerLastNameException.LONG_LASTNAME_MESSAGE); }
        }

        private void validateMoney(int money) throws BadCustomerAccountException {
            if(money < 0) { throw new BadCustomerAccountException(BadCustomerAccountException.NEGATIVE_ACCOUNT); }
        }

        private int evaluatePassword(String password) {
            String[] regexChecks = {".*[a-z]+.*", //lower
                    ".*[A-Z]+.*", //upper
                    ".*[\\d]+.*", //digits
                    ".*[@%#&.]+.*" //symbols
            };
            int res = 0;

            for (String reg : regexChecks)
                if (password.matches(reg))
                    res += 25;

            return res;
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

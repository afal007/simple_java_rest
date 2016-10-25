package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.routines.EmailValidator;

import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class User extends Entity<User.UserData> {
    private UUID customerId;
    private UUID[] subscriptionIds;
    private UUID id;

    public User(UserData data, UUID id, UUID customerId) throws BadUserException {
        super(data);
        this.id = id;
        this.customerId = customerId;
        data.validate();
    }

    public UUID getCustomerId() {
        return customerId;
    }
    public UUID getId() {
        return id;
    }
    public UUID[] getSubscriptionIds() {
        return subscriptionIds;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {
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
        @JsonProperty("userRole")
        private UserRole userRole;

        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }
        public String getPass() {
            return pass;
        }
        public void setPass(String pass) {
            this.pass = pass;
        }
        public UserRole getUserRole() {
            return userRole;
        }
        public void setUserRole(UserRole userRole) {
            this.userRole = userRole;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "UserData{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", login='" + login + '\'' +
                    ", pass='" + pass + '\'' +
                    ", userRole=" + userRole +
                    '}';
        }

        public static enum UserRole {
            COMPANY_ADMINISTRATOR("Company administrator"),
            TECHNICAL_ADMINISTRATOR("Technical administrator"),
            BILLING_ADMINISTRATOR("Billing administrator"),
            USER("User");

            private String roleName;

            UserRole(String roleName) {
                this.roleName = roleName;
            }

            public String getRoleName() {
                return roleName;
            }
        }

        public UserData(String firstName, String lastName, String login, String pass, UserRole userRole) throws BadUserException {
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.userRole = userRole;

            validate(firstName, lastName, login, pass);
        }

        private UserData() {
        }

        private void validate(String firstName, String lastName, String login, String pass) throws BadUserException {
            validatePassword(pass);
            validateLogin(login);
            validateFirstName(firstName);
            validateLastName(lastName);
        }

        private void validate() throws BadUserException {
            validate(firstName, lastName, login, pass);
        }

        private void validatePassword(String pass) throws BadUserException {
            if (pass == null || pass.equals("")) throw new BadUserException(BadUserException.PASSWORD_EMPTY);

            if (pass.length() < 6 || pass.length() > 12) throw new BadUserException(BadUserException.PASSWORD_LENGHT);

            if (evaluatePassword(pass) <= 50) throw new BadUserException(BadUserException.PASSWORD_SIMPLE);
            System.out.println(login);
            if (pass.toLowerCase().contains(login.toLowerCase()))
                throw new BadUserException(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
            if (pass.toLowerCase().contains(firstName.toLowerCase()))
                throw new BadUserException(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
            if (pass.toLowerCase().contains(lastName.toLowerCase()))
                throw new BadUserException(BadUserException.PASSWORD_CONTAINS_CREDENTIALS);
        }

        private void validateLogin(String login) throws BadUserException {
            if (!EmailValidator.getInstance().isValid(login)) {
                throw new BadUserException(BadUserException.EMAIL_INVALID);
            }
        }

        private void validateFirstName(String firstName) throws BadUserException {
            if (firstName.length() < 2 || firstName.length() > 12) {
                throw new BadUserException(BadUserException.FIRSTNAME_LENGHT);
            }
            if (!firstName.matches("[A-z]*")) {
                throw new BadUserException(BadUserException.FIRSTNAME_INVALID_CHARS);
            }
            if (!firstName.matches("^[A-Z][a-z]*")) {
                throw new BadUserException(BadUserException.FIRSTNAME_CAPITAL);
            }
        }

        private void validateLastName(String lastName) throws BadUserException {
            if (lastName.length() < 2 || lastName.length() > 12) {
                throw new BadUserException(BadUserException.LASTNAME_LENGHT);
            }
            if (!lastName.matches("[A-z]*")) {
                throw new BadUserException(BadUserException.LASTNAME_INVALID_CHARS);
            }
            if (!lastName.matches("^[A-Z][a-z]*")) {
                throw new BadUserException(BadUserException.LASTNAME_CAPITAL);
            }

        }

        private int evaluatePassword(String password) {
            String[] regexChecks = {".*[a-z]+.*", //lower
                    ".*[A-Z]+.*", //upper
                    ".*[\\d]+.*", //digits
                    ".*[@%#&]+.*" //symbols
            };
            int res = 0;

            for (String reg : regexChecks)
                if (password.matches(reg))
                    res += 25;

            return res;
        }
    }
}

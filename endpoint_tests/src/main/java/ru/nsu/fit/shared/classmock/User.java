package ru.nsu.fit.shared.classmock;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class User extends Entity<User.UserData>{
    public UUID customerId;
    public UUID[] subscriptionIds;
    public UUID id;

    public User(UserData data, UUID id, UUID customerId) {
        super(data);
        this.id = id;
        this.customerId = customerId;
    }

    public User() {}

    public static class UserData {
        public String firstName;
        public String lastName;
        public String login;
        public String pass;
        public UserRole userRole;

        public UserData(String firstName, String lastName, String login, String pass, UserRole userRole){
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.pass = pass;
        this.userRole = userRole;
    }

        public UserData() {}

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

            public static UserRole fromString(String text) {
                if (text != null) {
                    for (UserRole b : UserRole.values()) {
                        if (text.equalsIgnoreCase(b.roleName)) {
                            return b;
                        }
                    }
                }
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            UserData userData = (UserData) o;

            return new EqualsBuilder()
                    .append(firstName, userData.firstName)
                    .append(lastName, userData.lastName)
                    .append(login, userData.login)
                    .append(pass, userData.pass)
                    .append(userRole, userData.userRole)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(firstName)
                    .append(lastName)
                    .append(login)
                    .append(pass)
                    .append(userRole)
                    .toHashCode();
        }
    }
}

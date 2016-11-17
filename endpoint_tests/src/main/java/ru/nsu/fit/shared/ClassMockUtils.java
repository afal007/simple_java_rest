package ru.nsu.fit.shared;

import ru.nsu.fit.tests.BuildVerificationTest;

import java.util.UUID;

/**
 * Author: Alexander Fal (falalexandr007@gmail.com)
 */
public class ClassMockUtils {
    public static class Customer {
        public UUID id;
        public String firstName;
        public String lastName;
        public String login;
        public String pass;
        public int money;

        public Customer(UUID id ,String firstName, String lastName, String login, String pass, int money) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
        }
    }
    public static class User {
        public UUID customerId;
        public UUID[] subscriptionIds;
        public UUID id;
        public String firstName;
        public String lastName;
        public String login;
        public String pass;
        public UserRole userRole;

        public User(UUID id, UUID customerId, UUID[] subscriptionIds, String firstName, String lastName, String login, String pass, UserRole userRole) {
            this.id = id;
            this.customerId = customerId;
            this.subscriptionIds = subscriptionIds;
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.userRole = userRole;
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
    }
}

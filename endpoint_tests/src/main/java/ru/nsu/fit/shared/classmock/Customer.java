package ru.nsu.fit.shared.classmock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class Customer extends Entity<Customer.CustomerData> {
    @JsonProperty("id")
    public UUID id;

    public Customer(CustomerData data, UUID id) {
        super(data);
        this.id = id;
    }

    public Customer() {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomerData {
        @JsonProperty("firstName")
        public String firstName;
        @JsonProperty("lastName")
        public String lastName;
        @JsonProperty("login")
        public String login;
        @JsonProperty("pass")
        public String pass;
        @JsonProperty("money")
        public int money;

        public CustomerData() {}

        public CustomerData(String firstName, String lastName, String login, String pass, int money) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            CustomerData that = (CustomerData) o;

            return new EqualsBuilder()
                    .append(money, that.money)
                    .append(firstName, that.firstName)
                    .append(lastName, that.lastName)
                    .append(login, that.login)
                    .append(pass, that.pass)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(firstName)
                    .append(lastName)
                    .append(login)
                    .append(pass)
                    .append(money)
                    .toHashCode();
        }
    }
}


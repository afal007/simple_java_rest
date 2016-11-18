package ru.nsu.fit.shared.classmock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 */
public class Subscription extends Entity<Subscription.SubscriptionData>{
    public UUID id;
    public UUID customerId;
    public UUID planId;

    public Subscription(SubscriptionData data, UUID id, UUID customerId, UUID planId) {
        super(data);
        this.id = id;
        this.customerId = customerId;
        this.planId = planId;
    }

    public Subscription() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(customerId, that.customerId)
                .append(planId, that.planId)
                .append(data, that.data)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(customerId)
                .append(planId)
                .toHashCode();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubscriptionData {
        @JsonProperty("usedSeats")
        public int usedSeats;
        @JsonProperty("status")
        public Status status;
        
        public enum Status {
            PROVISIONING("Provisioning"),
            DONE("Done");

            public String statusName;

            Status(String status) {
                statusName = status;
            }

            public void setStatusName(String statusName) {
                this.statusName = statusName;
            }

            public String getStatusName() {
                return statusName;
            }
            
            public static Status fromString(String text) {
                if (text != null) {
                  for (Status b : Status.values()) {
                    if (text.equalsIgnoreCase(b.statusName)) {
                      return b;
                    }
                  }
                }
                return null;
            }
        }

        public SubscriptionData(Status status) {
            this.usedSeats = 0;
            this.status = status;
        }
        
        public SubscriptionData(){}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            SubscriptionData that = (SubscriptionData) o;

            return new EqualsBuilder()
                    .append(usedSeats, that.usedSeats)
                    .append(status, that.status)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(usedSeats)
                    .append(status)
                    .toHashCode();
        }
    }
}

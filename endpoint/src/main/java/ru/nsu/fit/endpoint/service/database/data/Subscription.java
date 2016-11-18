package ru.nsu.fit.endpoint.service.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 */
//ADD VALIDATION
public class Subscription extends Entity<Subscription.SubscriptionData>{
    private UUID id;
    private UUID customerId;
    private UUID PlanId;

    public Subscription(SubscriptionData data, UUID id, UUID customerId, UUID PlanId) {
        super(data);
        this.id = id;
        this.customerId = customerId;
        this.PlanId = PlanId;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getCustomerId() {
        return customerId;
    }
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
    public UUID getPlanId() {
        return PlanId;
    }
    public void setPlanId(UUID planId) {
        this.PlanId = planId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubscriptionData {
        @JsonProperty("usedSeats")
        private int usedSeats;
        @JsonProperty("status")
        private Status status;

        public int getUsedSeats() {
            return usedSeats;
        }
        public void setUsedSeats(int usedSeats) {
            this.usedSeats = usedSeats;
        }
        public Status getStatus() {
            return status;
        }
        public void setStatus(Status status) {
            this.status = status;
        }

        public enum Status {
            PROVISIONING("Provisioning"),
            DONE("Done");

            private String statusName;

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
        
        private SubscriptionData(){}


        @Override
        public String toString() {
            return "SubscriptionData{" +
                    "usedSeats=" + usedSeats +
                    ", status=" + status +
                    '}';
        }
    }
}

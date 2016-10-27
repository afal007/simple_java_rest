package ru.nsu.fit.endpoint.service.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 */
public class Subscription extends Entity<Subscription.SubscriptionData>{
    private UUID id;
    private UUID customerId;
    private UUID servicePlanId;

    public Subscription(SubscriptionData data, UUID id, UUID customerId, UUID servicePlanId) {
        super(data);
        this.id = id;
        this.customerId = customerId;
        this.servicePlanId = servicePlanId;
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
        return servicePlanId;
    }
    public void setPlanId(UUID servicePlanId) {
        this.servicePlanId = servicePlanId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubscriptionData {
        @JsonProperty("usedSeats")
        private int usedSeats;
        @JsonProperty("status")
        private Status status;

        public static enum Status {
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
        }

        public SubscriptionData(Status status) {
            this.usedSeats = 1;
            this.status = status;
        }
        
        private SubscriptionData(){}
        
        public int getUsedSeats(){
        	return this.usedSeats;
        }
        
        public Status getStatus(){
        	return this.status;
        }

        @Override
        public String toString() {
            return "SubscriptionData{" +
                    "usedSeats=" + usedSeats +
                    ", status=" + status +
                    '}';
        }
    }
}

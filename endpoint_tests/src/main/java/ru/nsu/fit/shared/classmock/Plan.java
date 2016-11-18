package ru.nsu.fit.shared.classmock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Plan extends Entity<Plan.PlanData> {
    @JsonProperty("id")
    public UUID id;
    @JsonProperty("external")
    public boolean isExternal;

    public Plan(PlanData data, UUID id){
        super(data);
        this.id = id;
        isExternal = isExternal();
    }
    public boolean isExternal() {
        return this.data.name.toLowerCase().contains("external");
    }

    public Plan() {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PlanData {
        @JsonProperty("name")
        public String name;
        @JsonProperty("details")
        public String details;
        @JsonProperty("maxSeats")
        public int maxSeats;
        @JsonProperty("feePerUnit")
        public int feePerUnit;
        @JsonProperty("cost")
        public int cost;

        public PlanData(String name, String details, int maxSeats, int feePerUnit, int cost){
            this.name = name;
            this.details = details;
            this.maxSeats = maxSeats;
            this.feePerUnit = feePerUnit;
            this.cost = cost;
        }
        
        public PlanData(){}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            PlanData planData = (PlanData) o;

            return new EqualsBuilder()
                    .append(maxSeats, planData.maxSeats)
                    .append(feePerUnit, planData.feePerUnit)
                    .append(cost, planData.cost)
                    .append(name, planData.name)
                    .append(details, planData.details)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(name)
                    .append(details)
                    .append(maxSeats)
                    .append(feePerUnit)
                    .append(cost)
                    .toHashCode();
        }
    }
}

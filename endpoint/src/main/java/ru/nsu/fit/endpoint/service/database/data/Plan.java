package ru.nsu.fit.endpoint.service.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.endpoint.service.database.exceptions.*;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Plan extends Entity<Plan.PlanData> {
    private UUID id;

    public Plan(PlanData data, UUID id) throws BadPlanException {
        super(data);
        this.id = id;
        data.validate();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isExternal() {
        return this.getData().getName().toLowerCase().contains("external");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PlanData {
        /* Длина не больше 128 символов и не меньше 2 включительно не содержит спец символов */
        @JsonProperty("name")
        private String name;
        /* Длина не больше 1024 символов и не меньше 1 включительно */
        @JsonProperty("details")
        private String details;
        /* Не больше 999999 и не меньше 1 включительно */
        @JsonProperty("maxSeats")
        private int maxSeats;
        /* Не больше 999999 и не меньше 1 включительно, minSeats <= maxSeats */
        @JsonProperty("minSeats")
        private int minSeats;
        /* Больше ли равно 0 но меньше либо равно 999999 */
        @JsonProperty("feePerUnit")
        private int feePerUnit;
        @JsonProperty("cost")
        private int cost;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDetails() {
            return details;
        }
        public void setDetails(String details) {
            this.details = details;
        }
        public int getMaxSeats() {
            return maxSeats;
        }
        public void setMaxSeats(int maxSeats) {
            this.maxSeats = maxSeats;
        }
        public int getMinSeats() {
            return minSeats;
        }
        public void setMinSeats(int minSeats) {
            this.minSeats = minSeats;
        }
        public int getFeePerUnit() {
            return feePerUnit;
        }
        public void setFeePerUnit(int feePerUnit) {
            this.feePerUnit = feePerUnit;
        }
        public int getCost() {
            return cost;
        }
        public void setCost(int cost) {
            this.cost = cost;
        }



        @Override
        public String toString() {
            return "PlanData{" +
                    "name='" + name + '\'' +
                    ", details='" + details + '\'' +
                    ", maxSeats=" + maxSeats +
                    ", minSeats=" + minSeats +
                    ", feePerUnit=" + feePerUnit +
                    ", cost=" + cost +
                    '}';
        }

        public PlanData(String name, String details, int maxSeats, int minSeats, int feePerUnit, int cost) throws BadPlanException {
            this.name = name;
            this.details = details;
            this.maxSeats = maxSeats;
            this.minSeats = minSeats;
            this.feePerUnit = feePerUnit;

            validate(name, details, maxSeats, minSeats, feePerUnit);
        }

        private void validate() throws BadPlanException {
            validate(name, details, maxSeats, minSeats, feePerUnit);
        }

        private void validate(String name, String details, int maxSeats, int minSeats, int feePerUnit) throws BadPlanException {
            validateName(name);
            validateDetails(details);
            validateMaxSeats(maxSeats, minSeats);
            validateMinSeats(minSeats);
            validateFee(feePerUnit);
        }

        private void validateName(String name) throws BadPlanNameException {
            if (name.length() > 128) throw new BadPlanNameException(BadPlanNameException.LONG_NAME_MESSAGE);
            if (name.length() < 2) throw new BadPlanNameException(BadPlanNameException.SHORT_NAME_MESSAGE);

            if (!name.matches("[A-z]*")) throw new BadPlanNameException(BadPlanNameException.WRONG_SYMBOLS_MESSAGE);
        }

        private void validateDetails(String details) throws BadPlanDetailsException {
            if (details.length() > 1024)
                throw new BadPlanDetailsException(BadPlanDetailsException.LONG_DETAILS_MESSAGE);
            if (details.length() < 1) throw new BadPlanDetailsException(BadPlanDetailsException.SHORT_DETAILS_MESSAGE);
        }

        private void validateMaxSeats(int maxSeats, int minSeats) throws BadPlanMaxSeatsException {
            if (maxSeats > 999999) throw new BadPlanMaxSeatsException(BadPlanMaxSeatsException.BIG_MAXSEATS_MESSAGE);
            if (maxSeats < 1) throw new BadPlanMaxSeatsException(BadPlanMaxSeatsException.SMALL_MAXSEATS_MESSAGE);
            if (minSeats > maxSeats)
                throw new BadPlanMaxSeatsException(BadPlanMaxSeatsException.LESS_THAN_MINSEATS_MESSAGE);
        }

        private void validateMinSeats(int minSeats) throws BadPlanMinSeatsException {
            if (minSeats > 999999) throw new BadPlanMinSeatsException(BadPlanMinSeatsException.BIG_MINSEATS_MESSAGE);
            if (minSeats < 1) throw new BadPlanMinSeatsException(BadPlanMinSeatsException.SMALL_MINSEATS_MESSAGE);
        }

        private void validateFee(int fee) throws BadPlanFeeException {
            if (fee < 0) throw new BadPlanFeeException(BadPlanFeeException.NEGATIVE_FEE_MESSAGE);
            if (fee > 999999) throw new BadPlanFeeException(BadPlanFeeException.BIG_FEE_MESSAGE);
        }

    }
}

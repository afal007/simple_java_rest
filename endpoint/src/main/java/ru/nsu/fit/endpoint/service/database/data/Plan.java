package ru.nsu.fit.endpoint.service.database.data;

import ru.nsu.fit.endpoint.service.database.exceptions.*;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Plan {
    private UUID id;
    /* Длина не больше 128 символов и не меньше 2 включительно не содержит спец символов */
    private String name;
    /* Длина не больше 1024 символов и не меньше 1 включительно */
    private String details;
    /* Не больше 999999 и не меньше 1 включительно */
    private int maxSeats;
    /* Не больше 999999 и не меньше 1 включительно, minSeats <= maxSeats */
    private int minSeats;
    /* Больше ли равно 0 но меньше либо равно 999999 */
    private int feePerUnit;

    public ServicePlan(String name, String details, int maxSeats, int minSeats, int feePerUnit) throws BadServicePlanException {
        validate(name, details, maxSeats, minSeats, feePerUnit);

        this.id = UUID.randomUUID();
        this.name = name;
        this.details = details;
        this.maxSeats = maxSeats;
        this.minSeats = minSeats;
        this.feePerUnit = feePerUnit;
    }

    private void validate(String name, String details, int maxSeats, int minSeats, int feePerUnit) throws BadServicePlanException {
        validateName(name);
        validateDetails(details);
        validateMaxSeats(maxSeats, minSeats);
        validateMinSeats(minSeats);
        validateFee(feePerUnit);
    }

    private void validateName(String name) throws BadServicePlanNameException {
        if(name.length() > 128) throw new BadServicePlanNameException( BadServicePlanNameException.LONG_NAME_MESSAGE );
        if(name.length() < 2) throw new BadServicePlanNameException( BadServicePlanNameException.SHORT_NAME_MESSAGE );

        if(!name.matches("[A-z]*")) throw new BadServicePlanNameException( BadServicePlanNameException.WRONG_SYMBOLS_MESSAGE );
    }

    private void validateDetails(String details) throws BadServicePlanDetailsException {
        if(details.length() > 1024) throw new BadServicePlanDetailsException( BadServicePlanDetailsException.LONG_DETAILS_MESSAGE );
        if(details.length() < 1) throw new BadServicePlanDetailsException( BadServicePlanDetailsException.SHORT_DETAILS_MESSAGE );
    }

    private void validateMaxSeats(int maxSeats, int minSeats) throws BadServicePlanMaxSeatsException {
        if(maxSeats > 999999) throw new BadServicePlanMaxSeatsException( BadServicePlanMaxSeatsException.BIG_MAXSEATS_MESSAGE );
        if(maxSeats < 1) throw new BadServicePlanMaxSeatsException( BadServicePlanMaxSeatsException.SMALL_MAXSEATS_MESSAGE );
        if(minSeats > maxSeats) throw new BadServicePlanMaxSeatsException(BadServicePlanMaxSeatsException.LESS_THAN_MINSEATS_MESSAGE);
    }

    private void validateMinSeats(int minSeats) throws BadServicePlanMinSeatsException {
        if(minSeats > 999999) throw new BadServicePlanMinSeatsException( BadServicePlanMinSeatsException.BIG_MINSEATS_MESSAGE );
        if(minSeats < 1) throw new BadServicePlanMinSeatsException( BadServicePlanMinSeatsException.SMALL_MINSEATS_MESSAGE );
    }

    private void validateFee(int fee) throws BadServicePlanFeeException {
        if(fee < 0) throw new BadServicePlanFeeException( BadServicePlanFeeException.NEGATIVE_FEE_MESSAGE );
        if(fee > 999999) throw new BadServicePlanFeeException( BadServicePlanFeeException.BIG_FEE_MESSAGE );
    }
}
